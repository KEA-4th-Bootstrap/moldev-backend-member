package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record ComposeProfileResultResponseDto(
        List<ComposeMemberProfileResponseDto> userList
) {
    public static ComposeProfileResultResponseDto of(List<ComposeMemberProfileResponseDto> userList) {
        return ComposeProfileResultResponseDto.builder()
                .userList(userList)
                .build();
    }
}
