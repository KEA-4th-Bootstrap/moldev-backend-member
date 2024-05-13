package org.bootstrap.member.controller;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/compose/members")
@RestController
public class MemberCompositionController {
    private final MemberService memberService;

    @GetMapping("/{moldevId}/profile")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(@PathVariable String moldevId) {
        final MemberProfileResponseDto responseDto = memberService.getMemberProfileForMoldevId(moldevId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
