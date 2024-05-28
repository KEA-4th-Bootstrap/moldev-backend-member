package org.bootstrap.member.dto.request;

import org.bootstrap.member.entity.ReasonType;

public record BanRequestDto(
    Long memberId,
    Integer banDays,
    ReasonType reason
) {
}
