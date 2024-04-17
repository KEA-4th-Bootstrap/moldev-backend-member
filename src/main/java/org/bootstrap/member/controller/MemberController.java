package org.bootstrap.member.controller;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.common.SuccessResponse;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getPost(@RequestHeader("Authorization") Long memberId) {
        final MemberProfileResponseDto responseDto = memberService.getMemberProfile(memberId);
        return SuccessResponse.ok(responseDto);
    }
}
