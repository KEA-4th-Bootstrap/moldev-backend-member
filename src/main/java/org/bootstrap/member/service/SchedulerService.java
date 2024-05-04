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
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class SchedulerService {

    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateViewCount() {
        Set<String> viewCountKeys = redisTemplate.keys("*");
        if (viewCountKeys == null) {
            return;
        }

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        List<Long> keys = getKeyList(viewCountKeys);
        List<Member> members = memberRepository.findAllById(keys);

        members.forEach(member -> {
            String key = String.valueOf(member.getId());
            String value = valueOperations.getAndDelete(key);
            assert value != null;
            member.updateViewCount(member.getViewCount() + Integer.parseInt(value));
        });

        memberRepository.saveAll(members);
    }

    private List<Long> getKeyList(Set<String> keys) {
        return keys.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}
