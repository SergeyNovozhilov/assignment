package com.luxoft.assignment.manager;

import com.luxoft.assignment.dao.ElvlDao;
import com.luxoft.assignment.dao.QuoteDao;
import com.luxoft.assignment.domain.Elvl;
import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.model.ElvlModel;
import com.luxoft.assignment.model.QuoteModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.luxoft.assignment.cache.CacheConfig.ELVL_CACHE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ManagerTest {
    @MockBean
    private QuoteDao quoteDao;
    @MockBean
    private ElvlDao elvlDao;
    @MockBean
    private CacheManager cacheManager;

    private List<ElvlModel> elvlModelList;
    private ElvlModel elvlModel;
    private QuoteModel quoteModel;

    private Cache cache;
    private Cache.ValueWrapper wrapper;

    @Configuration
    static class AuthorManagerConfiguration {
        @Autowired
        private QuoteDao quoteDao;
        @Autowired
        private ElvlDao elvlDao;
        @Autowired
        private CacheManager cacheManager;

        @Bean
        public Manager getManagerImpl() {
            return new ManagerImpl(quoteDao, elvlDao, cacheManager);
        }
    }

    @Autowired
    private ManagerImpl underTest;

    @Before
    public void setUp() {
        cache = mock(Cache.class);
        when(cacheManager.getCache(ELVL_CACHE)).thenReturn(cache);
        elvlModel = new ElvlModel("RU000A0JX0J3", 100.3);
        wrapper = mock(SimpleValueWrapper.class);
        when(cache.get(anyString())).thenReturn(wrapper);
        when(wrapper.get()).thenReturn(elvlModel);
    }

    @Test
    public void getAllTest() {
        underTest.get();
        verify(elvlDao, times(1)).get();
    }

    @Test
    public void getTest() {
        String isin = "RU000A0JX0J3";
        underTest.get(isin);
        verify(elvlDao, times(1)).get(isin);
    }

    @Test
    public void addNewQuoteTest() {
        quoteModel = new QuoteModel("RU000A0JX0J3", 100.5, 100.8);
        when(cache.get(anyString())).thenReturn(null);
        underTest.add(quoteModel);
        verify(elvlDao, times(1)).add(any(Isin.class));
        verify(elvlDao, times(1)).add(any(Elvl.class));
        verify(quoteDao, times(1)).add(quoteModel);
        verify(cache, times(1)).put(anyString(), any(ElvlModel.class));
    }

    @Test
    public void addQuoteBidToElvlTest() {
        quoteModel = new QuoteModel("RU000A0JX0J3", 100.5, 100.8);
        underTest.add(quoteModel);

        verify(elvlDao, times(1)).update(elvlModel);
        verify(quoteDao, times(1)).add(quoteModel);
        verify(cache, times(1)).put(anyString(), any(ElvlModel.class));

        assertEquals(elvlModel.getElvl(), quoteModel.getBid(), 0.01);
    }

    @Test
    public void addQuoteAskToElvlTest() {
        quoteModel = new QuoteModel("RU000A0JX0J3", 100.1, 100.2);
        underTest.add(quoteModel);

        verify(elvlDao, times(1)).update(elvlModel);
        verify(quoteDao, times(1)).add(quoteModel);
        verify(cache, times(1)).put(anyString(), any(ElvlModel.class));

        assertEquals(elvlModel.getElvl(), quoteModel.getAsk(), 0.01);
    }

    @Test
    public void addQuoteBidNullTest() {
        quoteModel = new QuoteModel("RU000A0JX0J3", null, 100.2);
        underTest.add(quoteModel);

        verify(elvlDao, times(1)).update(elvlModel);
        verify(quoteDao, times(1)).add(quoteModel);
        verify(cache, times(1)).put(anyString(), any(ElvlModel.class));

        assertEquals(elvlModel.getElvl(), quoteModel.getAsk(), 0.01);
    }

    @Test
    public void addQuoteIncorrectIsinTest() {
        quoteModel = new QuoteModel("RU000A0JX0", 100.5, 100.8);
        when(cache.get(anyString())).thenReturn(null);
        underTest.add(quoteModel);
        verify(elvlDao, times(0)).add(any(Isin.class));
        verify(elvlDao, times(0)).add(any(Elvl.class));
        verify(quoteDao, times(0)).add(quoteModel);
        verify(cache, times(0)).put(anyString(), any(ElvlModel.class));
    }

    @Test
    public void addQuoteBidBiggerAskTest() {
        quoteModel = new QuoteModel("RU000A0JX0", 100.5, 100.2);
        when(cache.get(anyString())).thenReturn(null);
        underTest.add(quoteModel);
        verify(elvlDao, times(0)).add(any(Isin.class));
        verify(elvlDao, times(0)).add(any(Elvl.class));
        verify(quoteDao, times(0)).add(quoteModel);
        verify(cache, times(0)).put(anyString(), any(ElvlModel.class));
    }
}
