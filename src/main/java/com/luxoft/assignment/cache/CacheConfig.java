package com.luxoft.assignment.cache;

import com.luxoft.assignment.dao.QuoteDao;
import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;
import com.luxoft.assignment.util.Util;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String ELVL_CACHE = "elvls";
    private QuoteDao quoteDao;

    public CacheConfig(QuoteDao quotelDao) {
        this.quoteDao = quotelDao;
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
            Map<String, List<Quote>> map = new HashMap<>();
            quoteDao.get().forEach(q -> {
                if (map.containsKey(q.getIsin())) {
                    map.get(q.getIsin()).add(q);
                } else {
                    List list = Collections.emptyList();
                    list.add(q);
                    map.put(q.getIsin(), list);
                }
            });
            map.forEach((k, v)-> {
                Collections.sort(v, (q1, q2) -> {
                    if (q1.getStmp().before(q2.getStmp())) {
                        return -1;
                    }
                    if (q1.getStmp().after(q2.getStmp())) {
                        return 1;
                    }
                    return 0;
                });
                Elvl elvl = new Elvl();
                elvl.setIsin(k);
                Util.setElvl(true, elvl, v.get(0));
                for (int i = 1; i < v.size(); i++) {
                    Util.setElvl(false, elvl, v.get(i));
                }
                this.getCache(ELVL_CACHE).put(k, elvl);
            });
        }
    }
}
