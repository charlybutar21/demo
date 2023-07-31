package com.charly.demo.repository;

import java.util.Optional;

public interface OTPCacheRepository {

    void put(String key, Integer value);

    Optional<String> get(String key);

    void remove(String key);
}
