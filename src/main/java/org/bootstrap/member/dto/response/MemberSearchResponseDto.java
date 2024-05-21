package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.bootstrap.member.entity.Member;

@Builder(access = AccessLevel.PRIVATE)
public record MemberSearchResponseDto(
        String moldevId,
        String profileImgUrl,
        String nickname,
        String islandName,
        Integer todayViewCount
) {
    public static MemberSearchResponseDto of(MemberProfileResponseDto member, Integer todayViewCount) {
        return MemberSearchResponseDto.builder()
                .moldevId(member.moldevId())
                .profileImgUrl(member.profileImgUrl())
                .nickname(member.nickname())
                .islandName(member.islandName())
                .todayViewCount(todayViewCount)
                .build();
    }
}
