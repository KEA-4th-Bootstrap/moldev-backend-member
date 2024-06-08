package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record BanDaysResponseDto(
    Long reportId,
    Integer banDays
) {
    public static BanDaysResponseDto of(Long reportId, Integer banDays) {
        return BanDaysResponseDto.builder()
                .reportId(reportId)
                .banDays(banDays)
                .build();
    }
}
