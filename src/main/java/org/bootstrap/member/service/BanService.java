package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.request.BanDaysRequestDto;
import org.bootstrap.member.dto.response.BanDaysListResponseDto;
import org.bootstrap.member.dto.response.BanDaysResponseDto;
import org.bootstrap.member.repository.BanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BanService {
    private final BanRepository banRepository;

    public BanDaysListResponseDto getBanInfoOfReport(BanDaysRequestDto banDaysRequestDto) {
        List<BanDaysResponseDto> bandaysList = banRepository.getBandaysList(banDaysRequestDto.reportIds());
        return BanDaysListResponseDto.of(bandaysList);
    }
}
