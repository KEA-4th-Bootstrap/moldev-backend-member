package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.bootstrap.member.entity.Member;

@Builder(access = AccessLevel.PRIVATE)
public record MyProfileResponseDto(
        String profileImgUrl,
        String moldevId,
        String email,
        String nickname,
        String islandName
) {
    public static MyProfileResponseDto of (Member member){
        return MyProfileResponseDto.builder()
                .profileImgUrl(member.getProfileImgUrl())
                .moldevId(member.getMoldevId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .islandName(member.getIslandName())
                .build();
    }

}
