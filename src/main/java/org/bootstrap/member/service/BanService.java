package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.BanInfoResponseDto;
import org.bootstrap.member.entity.Ban;
import org.bootstrap.member.exception.MemberNotFoundException;
import org.bootstrap.member.repository.BanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class BanService {
    private final BanRepository banRepository;

    public BanInfoResponseDto getBanInfoOfReport(Long reportId) {
        try {
            Ban ban = findByReportIdOrThrow(reportId);
            LocalDateTime unbanDate = ban.getUnbanDate();
            LocalDateTime lastModifiedDate = ban.getLastModifiedDate();
            Integer bandays = unbanDate.compareTo(lastModifiedDate);
            return BanInfoResponseDto.of(bandays);
        } catch (MemberNotFoundException e) {
            return BanInfoResponseDto.of(0);
        }
    }

    private Ban findByReportIdOrThrow(Long reportId) {
        return banRepository.findByReportId(reportId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}
