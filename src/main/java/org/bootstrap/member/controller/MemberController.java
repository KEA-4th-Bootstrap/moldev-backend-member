package org.bootstrap.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<MyProfileResponseDto> getMyProfile(@RequestHeader("Authorization") Long memberId) {
        final MyProfileResponseDto responseDto = memberService.getMyProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/my")
    public ResponseEntity<Void> patchMyProfile(@RequestHeader("Authorization") Long memberId,
                                                             @RequestBody ProfilePatchRequestDto profilePatchRequestDto) {
        memberService.patchMemberProfile(memberId, profilePatchRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/password")
    public ResponseEntity<Void> checkPassword(@RequestHeader("Authorization") Long memberId,
                                              @RequestBody PasswordCheckRequestDto passwordCheckRequestDto) {
        memberService.checkPassword(memberId, passwordCheckRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordPatchRequestDto passwordPatchRequestDto) {
        memberService.updatePassword(passwordPatchRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<Void> updateProfileImage(@RequestHeader("Authorization") Long memberId,
                                                                 @RequestPart(required = false) MultipartFile profileImage) {
        memberService.updateProfileImage(memberId, profileImage);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberProfileResponseDto> getMemberProfile(@PathVariable Long memberId) {
        final MemberProfileResponseDto responseDto = memberService.getMemberProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/view/{memberId}")
    public ResponseEntity<Void> viewCountUp(@PathVariable Long memberId,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        memberService.viewCountUpByCookie(memberId, request, response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/ban")
    public ResponseEntity<Void> banMember(@RequestBody BanRequestDto banRequestDto) {
        memberService.banMember(banRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/info")
    public ResponseEntity<ComposeProfileResultResponseDto> getMembersInfo(@RequestParam final List<Long> ids) {
        final ComposeProfileResultResponseDto response = memberService.getMembersProfile(ids);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<MemberInfoForAdminResponseDto>> getMembersInfoForAdmin(@RequestParam(required = false) Boolean marketingAgree,
                                                                     @RequestParam(required = false) String searchMoldevId,
                                                                     @PageableDefault Pageable pageable) {
        Page<MemberInfoForAdminResponseDto> membersInfoForAdmin = memberService.getMembersInfoForAdmin(marketingAgree, searchMoldevId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(membersInfoForAdmin);
    }

    @GetMapping("/trend")
    public ResponseEntity<List<TrendingMembersResponseDto>> getTrendingPosts() {
        final List<TrendingMembersResponseDto> responseDto = memberService.getTrendingMembersInfo();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/{moldevId}/profile")
    public ResponseEntity<ComposeMemberProfileResponseDto> getMemberProfile(@PathVariable String moldevId) {
        final ComposeMemberProfileResponseDto responseDto = memberService.getMemberProfileForMoldevId(moldevId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<MemberSearchResultResponseDto> getMemberSearch(@RequestParam String searchText) {
        final MemberSearchResultResponseDto responseDto = memberService.getMemberSearch(searchText);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecommendMemberProfileResponseDto> getMemberRecommend(@RequestParam List<Long> memberIds) {
        final RecommendMemberProfileResponseDto responseDto = memberService.getMemberRecommend(memberIds);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
