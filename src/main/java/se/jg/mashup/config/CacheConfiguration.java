package se.jg.mashup.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String CACHE_MANAGER = "caffeineCacheManager";

    @Bean
    public CacheManager caffeineCacheManager() {
        log.debug("Starting Caffeine Cache Manager");
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        CaffeineCache longTerm = new CaffeineCache("longTerm", Caffeine.newBuilder()
                .maximumSize(20000L) //TODO might need to adjust this depending on load
                .expireAfterAccess(24, TimeUnit.HOURS) // 24h should be enough
                .build());
        cacheManager.setCaches(Collections.singletonList(longTerm));
        return cacheManager;
    }
}
