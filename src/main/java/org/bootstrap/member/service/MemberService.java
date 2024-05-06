package org.bootstrap.member.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.aws.S3Service;
import org.bootstrap.member.dto.request.PasswordCheckRequestDto;
import org.bootstrap.member.dto.request.PasswordPatchRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.dto.response.MyProfileResponseDto;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.exception.PasswordWrongException;
import org.bootstrap.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.bootstrap.member.exception.MemberNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final RedisTemplate<String, String> redisTemplate;
    public static final String PROFILE_IMAGE_DIRECTORY = "profile";

    public MyProfileResponseDto getMyProfile(Long memberId) {
        Member member = findByIdOrThrow(memberId);
        return MyProfileResponseDto.of(member);
    }

    public MemberProfileResponseDto getMemberProfile(Long memberId){
        Member member = findByIdOrThrow(memberId);
        return MemberProfileResponseDto.of(member);
    }

    public void patchMemberProfile(Long memberId, ProfilePatchRequestDto profilePatchRequestDto){
        Member member = findByIdOrThrow(memberId);
        member.updateProfile(profilePatchRequestDto);
    }

    public void checkPassword(Long memberId, PasswordCheckRequestDto passwordCheckRequestDto){
        Member member = findByIdOrThrow(memberId);
        validatePassword(passwordCheckRequestDto.password(), member.getPassword());
    }

    public void updatePassword(Long memberId, PasswordPatchRequestDto passwordPatchRequestDto){
        Member member = findByIdOrThrow(memberId);
        String encodedPassword = encodePassword(passwordPatchRequestDto.password());
        member.updatePassword(encodedPassword);
    }

    public void updateProfileImage(Long memberId, MultipartFile profileImage){
        Member member = findByIdOrThrow(memberId);
        String profileImgUrl = checkProfileImageAndGetUrl(profileImage, member.getMoldevId());
        member.updateProfileImage(profileImgUrl);
    }

    public void viewCountUpByCookie(Long memberId, HttpServletRequest request, HttpServletResponse response){
        final String MEMBER_ID = String.valueOf(memberId);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElseGet(() -> new Cookie[0]);
        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("viewCount"))
                .findFirst()
                .orElseGet(() -> {
                    valueOperations.increment(MEMBER_ID, 1L);
                    return new Cookie("viewCount", MEMBER_ID);
                });

        if (!cookie.getValue().contains(MEMBER_ID)){
            valueOperations.increment(MEMBER_ID, 1L);
            cookie.setValue(cookie.getValue() + MEMBER_ID);
        }

        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/");
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }

    private void validatePassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw PasswordWrongException.EXCEPTION;
        }
    }

    private Member findByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
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
        return s3Service.getFileUrl(PROFILE_IMAGE_DIRECTORY + "/default.png");
    }

    private String generateProfileImageFileName(String originalFileName, String moldevId) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return moldevId + extension;
    }

    private void applyCookie(HttpServletResponse response) {

    }

}
