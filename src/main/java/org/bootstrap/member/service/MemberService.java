package org.bootstrap.member.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.bootstrap.member.exception.MemberNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;
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

}
