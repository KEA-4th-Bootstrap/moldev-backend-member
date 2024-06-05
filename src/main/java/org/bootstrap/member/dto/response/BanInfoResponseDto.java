package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BanInfoResponseDto(
    Integer banDays
) {
    public static BanInfoResponseDto of(Integer banDays) {
        return BanInfoResponseDto.builder()
                .banDays(banDays)
                .build();
    }
}
