package org.bootstrap.member.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.aws.S3Service;
import org.bootstrap.member.common.PageInfo;
import org.bootstrap.member.dto.request.BanRequestDto;
import org.bootstrap.member.dto.request.PasswordCheckRequestDto;
import org.bootstrap.member.dto.request.PasswordPatchRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.bootstrap.member.dto.response.*;
import org.bootstrap.member.entity.Ban;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.exception.MemberNotFoundException;
import org.bootstrap.member.exception.PasswordWrongException;
import org.bootstrap.member.repository.BanRepository;
import org.bootstrap.member.repository.MemberRepository;
import org.bootstrap.member.utils.CookieUtils;
import org.bootstrap.member.utils.RedisUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BanRepository banRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final RedisUtils redisUtils;
    public static final String PROFILE_IMAGE_DIRECTORY = "profile";
    public static final String MEMBER_VIEW_COUNT = "member_view_count";

    public MyProfileResponseDto getMyProfile(Long memberId) {
        Member member = findByIdOrThrow(memberId);
        return MyProfileResponseDto.of(member);
    }

    public MemberProfileResponseDto getMemberProfile(Long memberId) {
        Member member = findByIdOrThrow(memberId);
        return MemberProfileResponseDto.of(member);
    }

    public ComposeMemberProfileResponseDto getMemberProfileForMoldevId(String moldevId,
                                                                       HttpServletRequest request,
                                                                       HttpServletResponse response) {
        Member member = findByMoldevIdOrThrow(moldevId);
        viewCountUpByCookie(moldevId, request, response);
        return ComposeMemberProfileResponseDto.of(member);
    }

    public void patchMemberProfile(Long memberId, ProfilePatchRequestDto profilePatchRequestDto) {
        Member member = findByIdOrThrow(memberId);
        member.updateProfile(profilePatchRequestDto);
    }

    public void checkPassword(Long memberId, PasswordCheckRequestDto passwordCheckRequestDto) {
        Member member = findByIdOrThrow(memberId);
        validatePassword(passwordCheckRequestDto.password(), member.getPassword());
    }

    public void updatePassword(PasswordPatchRequestDto passwordPatchRequestDto) {
        Member member = findByEmailOrThrow(passwordPatchRequestDto.email());
        String encodedPassword = encodePassword(passwordPatchRequestDto.password());
        member.updatePassword(encodedPassword);
    }

    public void updateProfileImage(Long memberId, MultipartFile profileImage) {
        Member member = findByIdOrThrow(memberId);
        String profileImgUrl = checkProfileImageAndGetUrl(profileImage, member.getMoldevId());
        member.updateProfileImage(profileImgUrl);
    }

    public MemberSearchResultResponseDto getMemberSearch(String text, Pageable pageable) {
        Slice<MemberProfileResponseDto> memberSearchResults = findMemberSearchResult(text, pageable);
        List<MemberSearchResponseDto> memberSearchResponseDtoList = addTodayViewCountAtSearchResult(memberSearchResults.getContent());
        return MemberSearchResultResponseDto.of(memberSearchResponseDtoList, PageInfo.of(memberSearchResults));
    }

    public void viewCountUpByCookie(String moldevId, HttpServletRequest request, HttpServletResponse response) {
//        final String MEMBER_ID = String.valueOf(memberId);

        Cookie[] cookies = CookieUtils.getCookies(request);
        Cookie cookie = getViewCountCookieFromCookies(cookies);

        if (!cookie.getValue().contains(moldevId)) {
            redisUtils.getZSetOperations().incrementScore(MEMBER_VIEW_COUNT, moldevId, 1);
            cookie.setValue(cookie.getValue() + moldevId);
        }

        int maxAge = getMaxAge();
        CookieUtils.addCookieWithMaxAge(response, cookie, maxAge);
    }

    public ComposeProfileResultResponseDto getMembersProfile(List<Long> ids) {
        List<ComposeMemberProfileResponseDto> memberByIds = findMemberByIds(ids);
        return ComposeProfileResultResponseDto.of(memberByIds);
    }

    @Cacheable(value = "trendPostUser", key = "T(String).join(',', #ids)")
    public ComposeProfileResultResponseDto getMembersProfileByMoldevId(List<String> ids) {
        List<ComposeMemberProfileResponseDto> memberByIds = findMemberByMoldevIds(ids);
        return ComposeProfileResultResponseDto.of(memberByIds);
    }

    public void banMember(BanRequestDto banRequestDto) {
        Member member = findByMoldevIdOrThrow(banRequestDto.moldevId());
        LocalDateTime unbanDate = LocalDateTime.now().plusDays(banRequestDto.banDays());
        Ban ban = Ban.of(member, banRequestDto.reportId(), unbanDate, banRequestDto.reason());
        banRepository.save(ban);
    }

    public Page<MemberInfoForAdminResponseDto> getMembersInfoForAdmin(Boolean marketingAgree, String searchMoldevId, Pageable pageable) {
        return memberRepository.getMemberInfoForAdmin(marketingAgree, searchMoldevId, pageable);
    }

    @Cacheable(value = "trendUser", key = "'Trend'")
    public TrendingMembersListResponseDto getTrendingMembersInfo() {
        Set<String> trendingMemberIds = redisUtils.getTrendingMoldevIds(MEMBER_VIEW_COUNT);
        List<MemberProfileResponseDto> trendingMembers = memberRepository.getTrendingMembers(trendingMemberIds);
        return TrendingMembersListResponseDto.of(createTrendingMembersWithRedisDto(trendingMembers));
    }

    public RecommendMemberProfileResponseDto getMemberRecommend(List<Long> memberIds) {
        List<MemberProfileResponseDto> searchList = findMemberProfileResponseDtoByIds(memberIds);
        List<MemberSearchResponseDto> memberSearchResponseDtoList = addTodayViewCountAtSearchResult(searchList);
        return RecommendMemberProfileResponseDto.of(memberSearchResponseDtoList);
    }

    public void deleteMember(String moldevId) {
        Member member = findByMoldevIdOrThrow(moldevId);
        memberRepository.delete(member);
    }

    private List<TrendingMembersResponseDto> createTrendingMembersWithRedisDto(List<MemberProfileResponseDto> trendingMembers) {
        return trendingMembers.stream()
                .map(member -> {
                    Double viewCount = redisUtils.getZSetOperations().score(MEMBER_VIEW_COUNT, member.moldevId());
                    if (Objects.isNull(viewCount)) {
                        viewCount = 0.0;
                    }
                    return TrendingMembersResponseDto.of(member, viewCount.intValue());
                })
                .sorted(Comparator.comparing(TrendingMembersResponseDto::redisViewCount).reversed())
                .toList();
    }

    private List<MemberSearchResponseDto> addTodayViewCountAtSearchResult(List<MemberProfileResponseDto> responseDtoList) {
        return responseDtoList.stream()
                .map(memberProfileResponseDto -> {
                    Double viewCount = redisUtils.getZSetOperations().score(MEMBER_VIEW_COUNT, memberProfileResponseDto.moldevId());
                    Integer integerViewCount = Objects.isNull(viewCount) ? 0 : viewCount.intValue();
                    return MemberSearchResponseDto.of(memberProfileResponseDto, integerViewCount);
                })
                .collect(Collectors.toList());
    }

    private void validatePassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw PasswordWrongException.EXCEPTION;
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String checkProfileImageAndGetUrl(MultipartFile profileImage, String moldevId) {
        if (Objects.isNull(profileImage))
            return getDefaultProfileImgUrl();
        else
            return uploadProfileImage(profileImage, moldevId);
    }

    private String uploadProfileImage(MultipartFile profileImage, String moldevId) {
        String imageFileName = generateProfileImageFileName(Objects.requireNonNull(profileImage.getOriginalFilename()), moldevId);
        return s3Service.uploadFile(profileImage, imageFileName, PROFILE_IMAGE_DIRECTORY);
    }

    private String getDefaultProfileImgUrl() {
        return s3Service.getFileUrl(PROFILE_IMAGE_DIRECTORY + "/default_img.jpg");
    }

    private String generateProfileImageFileName(String originalFileName, String moldevId) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return moldevId + extension;
    }

    private void applyCookie(HttpServletResponse response, Cookie cookie) {
        int maxAge = getMaxAge();
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private Cookie getViewCountCookieFromCookies(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(MEMBER_VIEW_COUNT))
                .findFirst()
                .orElseGet(() -> CookieUtils.createCookie(MEMBER_VIEW_COUNT, ""));
    }

    private int getMaxAge() {
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return (int) (todayEndSecond - currentSecond);
    }

    private List<ComposeMemberProfileResponseDto> findMemberByIds(List<Long> ids) {
        List<Member> members = findMembersById(ids);
        return members.stream()
                .map(ComposeMemberProfileResponseDto::of)
                .collect(Collectors.toList());
    }

    private List<ComposeMemberProfileResponseDto> findMemberByMoldevIds(List<String> ids) {
        List<Member> members = findMembersByMoldevId(ids);
        return members.stream()
                .map(ComposeMemberProfileResponseDto::of)
                .collect(Collectors.toList());
    }

    private List<MemberProfileResponseDto> findMemberProfileResponseDtoByIds(List<Long> ids) {
        List<Member> members = findMembersById(ids);
        return members.stream()
                .map(MemberProfileResponseDto::of)
                .collect(Collectors.toList());
    }

    private List<Member> findMembersById(List<Long> ids) {
        return ids.stream()
                .map(this::findByIdOrThrow)
                .collect(Collectors.toList());
    }

    private List<Member> findMembersByMoldevId(List<String> ids) {
        return ids.stream()
                .map(this::findByMoldevIdOrThrow)
                .collect(Collectors.toList());
    }

    private Member findByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    private Member findByEmailOrThrow(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    private Member findByMoldevIdOrThrow(String moldevId) {
        return memberRepository.findByMoldevId(moldevId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

    private Slice<MemberProfileResponseDto> findMemberSearchResult(String text, Pageable pageable) {
        return memberRepository.findMemberSearchResult(text, pageable);
    }
}
