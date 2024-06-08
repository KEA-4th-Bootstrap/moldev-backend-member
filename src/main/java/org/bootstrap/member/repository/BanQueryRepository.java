package org.bootstrap.member.repository;

import org.bootstrap.member.dto.response.BanDaysResponseDto;

import java.util.List;

public interface BanQueryRepository {
    List<BanDaysResponseDto> getBandaysList(List<Long> reportIds);
}
