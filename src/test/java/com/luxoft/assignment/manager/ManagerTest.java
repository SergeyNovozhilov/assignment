package com.luxoft.assignment.manager;

import com.luxoft.assignment.dao.QuoteDao;
import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.luxoft.assignment.cache.CacheConfig.ELVL_CACHE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ManagerTest {
    @MockBean
    private QuoteDao quoteDao;
    @MockBean
    private CacheManager cacheManager;

    private List<Elvl> elvlList;
    private Elvl elvl;
    private Quote quote;

    private Cache cache;

    @Configuration
    static class ManagerConfiguration {
        @Autowired
        private QuoteDao quoteDao;
        @Autowired
        private CacheManager cacheManager;

        @Bean
        public Manager getManagerImpl() {
            return new ManagerImpl(quoteDao, cacheManager);
        }
    }

    @Autowired
    private ManagerImpl underTest;

    @Before
    public void setUp() {
        cache = mock(Cache.class);
        when(cacheManager.getCache(ELVL_CACHE)).thenReturn(cache);
        when(cache.getNativeCache()).thenReturn(new ConcurrentHashMap<String, Elvl>());
        elvl = new Elvl("RU000A0JX0J3", new BigDecimal(100.3));
        when(cache.get(anyString(), any(Class.class))).thenReturn(elvl);
    }

    @Test
    public void getAllTest() {
        underTest.get();
        verify(cache, times(1)).getNativeCache();
    }

    @Test
    public void getTest() {
        String isin = "RU000A0JX0J3";
        underTest.get(isin);
        verify(cache, times(1)).get(anyString(), any(Class.class));
    }

    @Test
    public void addNewQuoteTest() {
        quote = new Quote("RU000A0JX0J3", new BigDecimal(100.5), new BigDecimal(100.8));
        when(cache.get(anyString(), any(Class.class))).thenReturn(null);
        underTest.add(quote);
        verify(quoteDao, times(1)).add(quote);
        verify(cache, times(1)).put(anyString(), any(Elvl.class));
    }

    @Test
    public void addQuoteBidToElvlTest() {
        quote = new Quote("RU000A0JX0J3", new BigDecimal(100.5), new BigDecimal(100.8));
        underTest.add(quote);
        verify(quoteDao, times(1)).add(quote);
        assertEquals(elvl.getElvl(), quote.getBid());
    }

    @Test
    public void addQuoteAskToElvlTest() {
        quote = new Quote("RU000A0JX0J3", new BigDecimal(100.1), new BigDecimal(100.2));
        underTest.add(quote);
        verify(quoteDao, times(1)).add(quote);
        assertEquals(elvl.getElvl(), quote.getAsk());
    }

    @Test
    public void addQuoteBidNullTest() {
        quote = new Quote("RU000A0JX0J3", null, new BigDecimal(100.2));
        underTest.add(quote);
        verify(quoteDao, times(1)).add(quote);
        assertEquals(elvl.getElvl(), quote.getAsk());
    }

    @Test
    public void addQuoteIncorrectIsinTest() {
        quote = new Quote("RU000A0JX0", new BigDecimal(100.5), new BigDecimal(100.8));
        when(cache.get(anyString())).thenReturn(null);
        underTest.add(quote);
        verify(quoteDao, times(0)).add(quote);
    }

    @Test
    public void addQuoteBidBiggerAskTest() {
        quote = new Quote("RU000A0JX0", new BigDecimal(100.5), new BigDecimal(100.2));
        when(cache.get(anyString())).thenReturn(null);
        underTest.add(quote);
        verify(quoteDao, times(0)).add(quote);
    }
}
