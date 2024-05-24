package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record TrendingMembersListResponseDto(
        List<TrendingMembersResponseDto> trendingMembersResponseDtos
) {
    public static TrendingMembersListResponseDto of(List<TrendingMembersResponseDto> trendingMembersResponseDtos){
        return TrendingMembersListResponseDto.builder()
                .trendingMembersResponseDtos(trendingMembersResponseDtos)
                .build();
    }
}
