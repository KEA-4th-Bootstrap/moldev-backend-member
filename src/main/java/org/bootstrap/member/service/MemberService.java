package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.request.PasswordCheckRequestDto;
import org.bootstrap.member.dto.request.PasswordPatchRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.exception.PasswordWrongException;
import org.bootstrap.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.bootstrap.member.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberProfileResponseDto getMemberProfile(Long memberId) {
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

}
