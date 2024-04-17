package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.bootstrap.member.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    
    private final MemberRepository memberRepository;

    public MemberProfileResponseDto getMemberProfile(Long memberId) {
        Member member = findByIdOrThrow(memberId);
        return MemberProfileResponseDto.of(member);
    }

    private Member findByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }

}
