package org.bootstrap.member.dto.request;

import org.bootstrap.member.entity.ReasonType;

public record BanRequestDto(
    String moldevId,
    Long reportId,
    Integer banDays,
    ReasonType reason
) {
}
