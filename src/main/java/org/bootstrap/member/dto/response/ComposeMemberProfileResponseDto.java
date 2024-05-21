package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.bootstrap.member.entity.Member;

@Builder(access = AccessLevel.PRIVATE)
public record ComposeMemberProfileResponseDto(
        String profileImgUrl,
        String moldevId,
        String nickname,
        String islandName
) {
    public static ComposeMemberProfileResponseDto of(Member member) {
        return ComposeMemberProfileResponseDto.builder()
                .profileImgUrl(member.getProfileImgUrl())
                .moldevId(member.getMoldevId())
                .nickname(member.getNickname())
                .islandName(member.getIslandName())
                .build();
    }
}
