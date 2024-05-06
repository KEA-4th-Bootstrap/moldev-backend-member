package org.bootstrap.member.service;

import lombok.RequiredArgsConstructor;
import org.bootstrap.member.entity.Member;
import org.bootstrap.member.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchedulerService {

    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleViewCount() {
        Optional.ofNullable(redisTemplate.keys("*"))
                .ifPresent(this::processKeys);
    }

    private void processKeys(Set<String> viewCountKeys) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        List<Long> keys = getKeyList(viewCountKeys);
        List<Member> members = getExistMembersByKeys(keys);

        members.forEach(member -> updateMemberViewCount(member, valueOperations));

        memberRepository.saveAll(members);
    }

    private void updateMemberViewCount(Member member, ValueOperations<String, String> valueOperations) {
        String key = String.valueOf(member.getId());
        String value = valueOperations.getAndDelete(key);
        if (value != null) {
            member.updateViewCount(member.getViewCount() + Integer.parseInt(value));
        }
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
