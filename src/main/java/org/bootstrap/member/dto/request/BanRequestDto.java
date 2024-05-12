package org.bootstrap.member.dto.request;

public record BanRequestDto(
    Long memberId,
    Integer banDays,
    Integer reasonCode
) {
}
