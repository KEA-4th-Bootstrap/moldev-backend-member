package org.bootstrap.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.BanDaysResponseDto;
import org.bootstrap.member.entity.Ban;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.bootstrap.member.entity.QBan.ban;

@Repository
@RequiredArgsConstructor
public class BanQueryRepositoryImpl implements BanQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BanDaysResponseDto> getBandaysList(List<Long> reportIds) {
        List<Ban> banList = jpaQueryFactory.selectFrom(ban)
                .where(inReportIds(reportIds))
                .fetch();

        Set<Long> existingReportIds = banList.stream()
                .map(Ban::getReportId)
                .collect(Collectors.toSet());

        List<Long> notFoundReportIds = reportIds.stream()
                .filter(reportId -> !existingReportIds.contains(reportId))
                .collect(Collectors.toList());

        List<BanDaysResponseDto> bandaysList = banList.stream()
                .map(ban -> {
                    LocalDateTime unbanDate = ban.getUnbanDate();
                    LocalDateTime lastModifiedDate = ban.getLastModifiedDate();
                    Integer bandays = unbanDate.compareTo(lastModifiedDate);
                    return BanDaysResponseDto.of(ban.getReportId(), bandays);
                })
                .collect(Collectors.toList());

        bandaysList.addAll(notFoundReportIds.stream()
                .map(reportId -> BanDaysResponseDto.of(reportId, 0))
                .toList());

        return bandaysList;
    }

    private BooleanExpression inReportIds(List<Long> reportIds) {
        return reportIds != null ? ban.reportId.in(reportIds) : null;
    }
}
