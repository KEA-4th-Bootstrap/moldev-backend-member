package org.bootstrap.member.common;

import lombok.Builder;
import org.springframework.data.domain.Slice;


@Builder
public record PageInfo (
        Boolean hasNextPage,
        Integer pageNumber
){
    public static PageInfo of(Slice<?> page) {
        return PageInfo.builder()
                .hasNextPage(page.hasNext())
                .pageNumber(page.getNumber())
                .build();
    }
}
