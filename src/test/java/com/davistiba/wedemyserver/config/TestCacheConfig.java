package com.davistiba.wedemyserver.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}
