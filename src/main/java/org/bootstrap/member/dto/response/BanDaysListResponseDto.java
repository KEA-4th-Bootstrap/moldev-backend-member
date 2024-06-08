package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record BanDaysListResponseDto(
        List<BanDaysResponseDto> banDaysResponseDto
) {
    public static BanDaysListResponseDto of(List<BanDaysResponseDto> banDaysResponseDto) {
        return BanDaysListResponseDto.builder()
                .banDaysResponseDto(banDaysResponseDto)
                .build();
    }
}
