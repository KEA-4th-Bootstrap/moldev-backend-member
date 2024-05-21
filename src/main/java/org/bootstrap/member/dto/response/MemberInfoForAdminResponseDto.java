package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record MemberInfoForAdminResponseDto(
        Long memberId,
        String email,
        String moldevId,
        String nickname,
        String islandName,
        Boolean isMarketingAgree
) {
}
