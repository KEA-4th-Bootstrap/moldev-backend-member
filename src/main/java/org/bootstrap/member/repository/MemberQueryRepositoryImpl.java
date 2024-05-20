package org.bootstrap.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.bootstrap.member.dto.response.MemberInfoForAdminResponseDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.bootstrap.member.dto.response.TrendingMembersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static org.bootstrap.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepositoryImpl implements MemberQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MemberInfoForAdminResponseDto> getMemberInfoForAdmin(Boolean marketingAgree, String searchMoldevId, Pageable pageable) {
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
                .where(
                        eqMarketingAgree(marketingAgree),
                        containsMoldevId(searchMoldevId)
                )
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

    @Override
    public List<MemberProfileResponseDto> getTrendingMembers(Set<Long> memberIds) {
        return jpaQueryFactory
                .select(Projections.constructor(MemberProfileResponseDto.class,
                        member.id,
                        member.profileImgUrl,
                        member.moldevId,
                        member.nickname,
                        member.islandName
                ))
                .from(member)
                .where(inMemberIds(memberIds))
                .fetch();
    }

    private BooleanExpression eqMarketingAgree(Boolean marketingAgree) {
        return marketingAgree != null ? member.isMarketingAgree.eq(marketingAgree) : null;
    }

    private BooleanExpression containsMoldevId(String searchMoldevId) {
        return searchMoldevId != null ? member.moldevId.contains(searchMoldevId) : null;
    }

    private BooleanExpression inMemberIds(Set<Long> memberIds) {
        return memberIds != null ? member.id.in(memberIds) : null;
    }
}