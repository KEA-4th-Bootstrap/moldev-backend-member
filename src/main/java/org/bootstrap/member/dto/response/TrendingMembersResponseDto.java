package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TrendingMembersResponseDto(
        MemberProfileResponseDto memberProfileResponseDto,
        Integer redisViewCount
) {
    public static TrendingMembersResponseDto of(MemberProfileResponseDto memberProfileResponseDto, Integer redisViewCount) {
        return TrendingMembersResponseDto.builder()
                .memberProfileResponseDto(memberProfileResponseDto)
                .redisViewCount(redisViewCount)
                .build();
    }
}
