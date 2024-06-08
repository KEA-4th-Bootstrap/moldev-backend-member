package org.bootstrap.member.dto.request;

import java.util.List;

public record BanDaysRequestDto(
    List<Long> reportIds
) {
}
