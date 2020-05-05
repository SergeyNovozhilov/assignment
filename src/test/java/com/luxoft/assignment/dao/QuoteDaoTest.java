package com.luxoft.assignment.dao;

import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.model.ElvlModel;
import com.luxoft.assignment.model.QuoteModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import({QuoteDaoImpl.class, ElvlDaoImpl.class})
public class QuoteDaoTest {
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private ElvlDao elvlDao;

    private Isin isin;
    private QuoteModel expected;
    private final double bid = 100.2;
    private final double ask = 100.9;

    @Before
    public void setUp() {
        isin = new Isin("RU000A0JX0J2");
        elvlDao.add(isin);
        expected = new QuoteModel(isin.getIsin(), bid, ask);
        quoteDao.add(expected);
    }

    @Test
    public void getQuoteTest() {
        List<QuoteModel> actual = quoteDao.get(isin.getIsin());

        assertFalse(actual == null);
        assertTrue(actual.size() == 1);
        assertEquals(expected.getIsin(), actual.get(0).getIsin());
        assertEquals(expected.getAsk(), actual.get(0).getAsk(), 0.01);
    }

    @Test
    public void get2QuoteTest() {
        quoteDao.add(new QuoteModel(isin.getIsin(), bid, ask));
        List<QuoteModel> actual = quoteDao.get(isin.getIsin());

        assertFalse(actual == null);
        assertTrue(actual.size() == 2);
        actual.forEach(q -> {
            assertEquals(expected.getIsin(), q.getIsin());
            assertEquals(expected.getAsk(), q.getAsk(), 0.01);
        });

    }

    @Test
    public void getAllQuoteTest() {
        Isin newIsin = new Isin("RU000A0JX0J3");
        elvlDao.add(newIsin);
        quoteDao.add(new QuoteModel(newIsin.getIsin(), bid, ask));
        List<QuoteModel> actual = quoteDao.get();

        assertFalse(actual == null);
        assertTrue(actual.size() == 2);

        assertTrue(actual.stream().map(QuoteModel::getIsin).collect(Collectors.toList()).contains("RU000A0JX0J2"));
        assertTrue(actual.stream().map(QuoteModel::getIsin).collect(Collectors.toList()).contains("RU000A0JX0J3"));

    }
}
