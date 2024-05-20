package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.repository.MemberRepository;
import org.bootstrap.member.utils.RedisUtils;
import org.springframework.data.redis.core.ZSetOperations;
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
        List<Long> keys = getKeyList(viewCountKeys);
        List<Member> members = getExistMembersByKeys(keys);

        members.forEach(this::updateMemberViewCount);
    }

    private void updateMemberViewCount(Member member) {
        String key = String.valueOf(member.getId());
        ZSetOperations<String, String> zSetOperations = redisUtils.getZSetOperations();
        Double viewCount = Objects.requireNonNull(zSetOperations.score(MEMBER_VIEW_COUNT, key));
        member.updateViewCount(member.getViewCount() + viewCount.intValue());
        zSetOperations.remove(MEMBER_VIEW_COUNT, key);
    }

    private List<Member> getExistMembersByKeys(List<Long> keys) {
        return keys.stream()
                .map(memberRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private List<Long> getKeyList(Set<String> keys) {
        return keys.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
