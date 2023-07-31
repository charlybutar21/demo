package com.charly.demo.repository;

import com.charly.demo.exception.OtpCacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class OTPCacheRepositoryImpl implements OTPCacheRepository{

    @Value("${spring.cache.redis.time-to-live}")
    private long ttl;
    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOps;

    @Autowired
    public OTPCacheRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOps = redisTemplate.opsForValue();
    }

    @Override
    public void put(String key, Integer value) {
        try {
            valueOps.set(key, String.valueOf(value));
            redisTemplate.expire(key, ttl, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new OtpCacheException("Error while saving to cache "+ e);
        }

    }

    @Override
    public Optional<String> get(String key) {
        try {
            Boolean b = redisTemplate.hasKey(key);
            if (Boolean.TRUE.equals(b)) {
                return Optional.ofNullable(valueOps.get(key));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new OtpCacheException("Error while retrieving from the cache ", e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new OtpCacheException("Error while removing from the cache ", e);
        }
    }
}
