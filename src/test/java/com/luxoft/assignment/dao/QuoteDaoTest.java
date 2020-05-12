package com.luxoft.assignment.dao;

import com.luxoft.assignment.model.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({QuoteDaoImpl.class})
public class QuoteDaoTest {
    @Autowired
    private QuoteDao quoteDao;

    private Quote expected;
    private String isin = "RU000A0JX0J2";
    private final BigDecimal bid = BigDecimal.valueOf(100.2d);
    private final BigDecimal ask = BigDecimal.valueOf(100.9d);

    @Before
    public void setUp() {
        expected = new Quote(isin, bid, ask, null);
        quoteDao.add(expected);
    }

    @Test
    public void getQuoteTest() {
        List<Quote> actual = quoteDao.get(isin);

        assertFalse(actual == null);
        assertTrue(actual.size() == 1);
        assertEquals(expected.getIsin(), actual.get(0).getIsin());
        assertEquals(expected.getAsk(), actual.get(0).getAsk());
    }

    @Test
    public void get2QuoteTest() {
        quoteDao.add(new Quote(isin, bid, ask, null));
        List<Quote> actual = quoteDao.get(isin);

        assertFalse(actual == null);
        assertTrue(actual.size() == 2);
        actual.forEach(q -> {
            assertEquals(expected.getIsin(), q.getIsin());
            assertEquals(expected.getAsk(), q.getAsk());
        });

    }

    @Test
    public void getAllQuoteTest() {
        quoteDao.add(new Quote("RU000A0JX0J3", bid, ask, null));
        List<Quote> actual = quoteDao.get();

        assertFalse(actual == null);
        assertTrue(actual.size() == 2);

        assertTrue(actual.stream().map(Quote::getIsin).collect(Collectors.toList()).contains("RU000A0JX0J2"));
        assertTrue(actual.stream().map(Quote::getIsin).collect(Collectors.toList()).contains("RU000A0JX0J3"));

    }
}
