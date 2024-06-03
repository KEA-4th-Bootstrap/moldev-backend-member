package org.bootstrap.member.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, ?> redisTemplate() {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public CacheManager trendPostCacheManager() {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()  // defaultCacheConfig() 는 기본값으로 설정된 RedisCacheConfiguration 객체 리턴
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 캐시 데이터 key 직렬화 시 문자열로 변환하여 저장
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));  // 캐시 데이터 value 직렬화할 때 JSON 으로 변환하여 저장

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();
        configurations.put("trendPostUser", defaultConfig.entryTtl(Duration.ofSeconds(298)));
        configurations.put("trendUser", defaultConfig.entryTtl(Duration.ofMinutes(5)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())  // RedisCacheManager 가 사용할 RedisConnectionFactory 객체 설정
                .cacheDefaults(defaultConfig) // RedisCacheConfiguration 인자를 사용하여 RedisCacheManager 의 기본 캐시 설정
                .withInitialCacheConfigurations(configurations) // RedisCacheManager 생성 시 초기값 설정, 따라서 RedisCacheManager 스프링 빈이 생성되면 hotelCache 와 hotelAddressCache 캐시 값이 설정됨
                .build();
    }
}
