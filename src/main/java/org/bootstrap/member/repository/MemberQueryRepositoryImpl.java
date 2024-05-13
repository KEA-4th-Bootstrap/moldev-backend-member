package org.bootstrap.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.MemberInfoForAdminResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.bootstrap.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MemberInfoForAdminResponseDto> getMemberInfoForAdmin(Pageable pageable) {
        List<MemberInfoForAdminResponseDto> memberList = jpaQueryFactory
                .select(Projections.constructor(MemberInfoForAdminResponseDto.class,
                        member.id,
                        member.email,
                        member.moldevId,
                        member.nickname,
                        member.islandName,
                        member.isMarketingAgree
                ))
                .from(member)
                .orderBy(member.id.desc())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

        Long memberCount = jpaQueryFactory
                .select(member.count())
                .from(member)
                .fetchFirst();

        return PageableExecutionUtils.getPage(memberList, pageable, () -> memberCount);
    }
}
