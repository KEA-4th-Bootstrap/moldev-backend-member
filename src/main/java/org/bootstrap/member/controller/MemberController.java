package org.bootstrap.member.controller;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.common.SuccessResponse;
import org.bootstrap.member.dto.request.PasswordCheckRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyProfile(@RequestHeader("Authorization") Long memberId) {
        final MemberProfileResponseDto responseDto = memberService.getMemberProfile(memberId);
        return SuccessResponse.ok(responseDto);
    }

    @PatchMapping("/my")
    public ResponseEntity<SuccessResponse<?>> patchMyProfile(@RequestHeader("Authorization") Long memberId,
                                                             @RequestBody ProfilePatchRequestDto profilePatchRequestDto){
        memberService.patchMemberProfile(memberId, profilePatchRequestDto);
        return SuccessResponse.ok(null);
    }

    @PostMapping("/password")
    public ResponseEntity<SuccessResponse<?>> checkPassword(@RequestHeader("Authorization") Long memberId,
                                                            @RequestBody PasswordCheckRequestDto passwordCheckRequestDto){
        memberService.checkPassword(memberId, passwordCheckRequestDto);
        return SuccessResponse.ok(null);
    }

}
