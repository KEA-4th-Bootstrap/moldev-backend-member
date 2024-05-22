package org.bootstrap.member.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record RecommendMemberProfileResponseDto(
        List<MemberSearchResponseDto> searchList
) {
    public static RecommendMemberProfileResponseDto of(List<MemberSearchResponseDto> searchList) {
        return RecommendMemberProfileResponseDto.builder()
                .searchList(searchList)
                .build();
    }
}
