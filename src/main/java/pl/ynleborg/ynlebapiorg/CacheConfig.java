package pl.ynleborg.ynlebapiorg;

import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Slf4j
@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager getAchievementsCM() {
        return new ConcurrentMapCacheManager("getAchievements") {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name,
                        CacheBuilder
                                .newBuilder()
                                .expireAfterWrite(30, TimeUnit.SECONDS)
                                .maximumSize(10000)
                                .build()
                                .asMap(),
                        false);
            }
        };
    }

    @Bean
    @Primary
    public CacheManager getXuidCM() {
        return new ConcurrentMapCacheManager("getXuid") {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name,
                        CacheBuilder
                                .newBuilder()
                                .expireAfterWrite(30, TimeUnit.DAYS)
                                .maximumSize(10)
                                .build()
                                .asMap(),
                        false);
            }
        };
    }
}
