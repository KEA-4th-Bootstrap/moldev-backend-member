package org.bootstrap.member.dto.request;

public record ProfilePatchRequestDto(
        String nickname,
        String islandName
) {
}
