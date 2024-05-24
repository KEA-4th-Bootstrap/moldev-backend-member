package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.repository.MemberRepository;
import org.bootstrap.member.utils.RedisUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchedulerService {

    private final static String MEMBER_VIEW_COUNT = "member_view_count";
    private final RedisUtils redisUtils;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleViewCount() {
        Optional.ofNullable(redisUtils.getZSetOperations().range(MEMBER_VIEW_COUNT, 0, -1))
                .ifPresent(this::processKeys);
    }

    private void processKeys(Set<String> viewCountKeys) {
        List<Member> members = getExistMembersByKeys(viewCountKeys);
        members.forEach(this::updateMemberViewCount);
    }

    private void updateMemberViewCount(Member member) {
        String moldevId = member.getMoldevId();
        Double viewCount = Objects.requireNonNull(redisUtils.getZSetOperations().score(MEMBER_VIEW_COUNT, moldevId));
        member.updateViewCount(member.getViewCount() + viewCount.intValue());
        redisUtils.getZSetOperations().remove(MEMBER_VIEW_COUNT, moldevId);
    }

    private List<Member> getExistMembersByKeys(Set<String> keys) {
        return keys.stream()
                .map(memberRepository::findByMoldevId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
