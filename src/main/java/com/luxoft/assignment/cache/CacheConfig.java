package com.luxoft.assignment.cache;

import com.luxoft.assignment.dao.ElvlDao;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String ELVL_CACHE = "elvls";
    private ElvlDao elvlDao;

    public CacheConfig(ElvlDao elvlDao) {
        this.elvlDao = elvlDao;
    }

    @Bean
    public CacheManager cacheManager() {
        return new MyConcurrentMapCacheManager(ELVL_CACHE);
    }

    class MyConcurrentMapCacheManager extends ConcurrentMapCacheManager {
        public MyConcurrentMapCacheManager(String... cacheNames) {
            super(cacheNames);
        }

        @PostConstruct
        public void init() {
            elvlDao.get().forEach(e -> this.getCache(ELVL_CACHE).put(e.getIsin(), e));
        }
    }
}
