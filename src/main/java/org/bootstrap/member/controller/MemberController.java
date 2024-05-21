package org.bootstrap.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.common.SuccessResponse;
import org.bootstrap.member.dto.request.BanRequestDto;
import org.bootstrap.member.dto.request.PasswordCheckRequestDto;
import org.bootstrap.member.dto.request.PasswordPatchRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.bootstrap.member.dto.response.*;
import org.bootstrap.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<?>> getMyProfile(@RequestHeader("Authorization") Long memberId) {
        final MyProfileResponseDto responseDto = memberService.getMyProfile(memberId);
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

    @PatchMapping("/password")
    public ResponseEntity<SuccessResponse<?>> updatePassword(@RequestHeader("Authorization") Long memberId,
                                                             @RequestBody PasswordPatchRequestDto passwordPatchRequestDto){
        memberService.updatePassword(memberId, passwordPatchRequestDto);
        return SuccessResponse.ok(null);
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<SuccessResponse<?>> updateProfileImage(@RequestHeader("Authorization") Long memberId,
                                                                 @RequestPart(required = false) MultipartFile profileImage){
        memberService.updateProfileImage(memberId, profileImage);
        return SuccessResponse.ok(null);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SuccessResponse<?>> getMemberProfile(@PathVariable Long memberId) {
        final MemberProfileResponseDto responseDto = memberService.getMemberProfile(memberId);
        return SuccessResponse.ok(responseDto);
    }

    @PostMapping("/view/{memberId}")
    public ResponseEntity<SuccessResponse<?>> viewCountUp(@PathVariable Long memberId,
                                                                  HttpServletRequest request,
                                                                  HttpServletResponse response){
        memberService.viewCountUpByCookie(memberId, request, response);
        return SuccessResponse.ok(null);
    }

    @PostMapping("/ban")
    public ResponseEntity<SuccessResponse<?>> banMember(@RequestBody BanRequestDto banRequestDto){
        memberService.banMember(banRequestDto);
        return SuccessResponse.ok(null);
    }

    @GetMapping("/info")
    public ResponseEntity<SuccessResponse<?>> getMembersInfo(final @RequestParam List<Long> ids) {
        final List<MemberProfileResponseDto> response = memberService.getMembersProfile(ids);
        return SuccessResponse.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<SuccessResponse<?>> getMembersInfoForAdmin(@RequestParam(required = false) Boolean marketingAgree,
                                                                     @RequestParam(required = false) String searchMoldevId,
                                                                     @PageableDefault Pageable pageable) {
        Page<MemberInfoForAdminResponseDto> membersInfoForAdmin = memberService.getMembersInfoForAdmin(marketingAgree, searchMoldevId, pageable);
        return SuccessResponse.ok(membersInfoForAdmin);
    }

    @GetMapping("/trend")
    public ResponseEntity<SuccessResponse<?>> getTrendingPosts() {
        final List<TrendingMembersResponseDto> responseDto = memberService.getTrendingMembersInfo();
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("/{moldevId}/profile")
    public ResponseEntity<ComposeMemberProfileResponseDto> getMemberProfile(@PathVariable String moldevId) {
        final ComposeMemberProfileResponseDto responseDto = memberService.getMemberProfileForMoldevId(moldevId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
