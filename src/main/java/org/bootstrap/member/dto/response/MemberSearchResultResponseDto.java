package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.bootstrap.member.common.PageInfo;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record MemberSearchResultResponseDto(
        List<MemberSearchResponseDto> searchList,
        PageInfo pageInfo
) {
    public static MemberSearchResultResponseDto of(List<MemberSearchResponseDto> searchList, PageInfo pageInfo) {
        return MemberSearchResultResponseDto.builder()
                .searchList(searchList)
                .pageInfo(pageInfo)
                .build();
    }
}
