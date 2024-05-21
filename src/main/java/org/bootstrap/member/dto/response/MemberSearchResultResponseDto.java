package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MemberSearchResultResponseDto(
        List<MemberSearchResponseDto> searchList
) {
    public static MemberSearchResultResponseDto of(List<MemberSearchResponseDto> searchList) {
        return MemberSearchResultResponseDto.builder()
                .searchList(searchList)
                .build();
    }
}
