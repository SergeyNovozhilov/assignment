package com.luxoft.assignment.dao;

import com.luxoft.assignment.domain.Elvl;
import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.model.ElvlModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@JdbcTest
@Import(ElvlDaoImpl.class)
public class ElvlDaoTest {
    @Autowired
    private ElvlDao elvlDao;

    private ElvlModel expected;
    private Elvl elvl;
    private Isin isin;



    @Before
    public void setUp() {
        isin = new Isin("RU000A0JX0J2");
        elvlDao.add(isin);
        elvl = new Elvl(isin.getId(), 100.05);
        elvlDao.add(elvl);
        expected = new ElvlModel(isin.getIsin(), elvl.getElvl());
    }

    @Test
    public void getElvlTest() {
        ElvlModel actual = elvlDao.get(isin.getIsin());

        assertFalse(actual == null);
        assertEquals(expected.getIsin(), actual.getIsin());
        assertEquals(expected.getElvl(), actual.getElvl(), 0.01);
    }

    @Test
    public void getAllElvlTest() {
        List<ElvlModel> actual = elvlDao.get();

        assertFalse(actual == null);
        assertTrue(actual.size() == 1);
        assertEquals(expected.getIsin(), actual.get(0).getIsin());
        assertEquals(expected.getElvl(), actual.get(0).getElvl(), 0.01);
    }

    @Test
    public void updateElvlTest() {
        double elvl = 200.9;
        expected.setElvl(elvl);
        elvlDao.update(expected);

        ElvlModel actual = elvlDao.get(isin.getIsin());

        assertFalse(actual == null);
        assertEquals(expected.getIsin(), actual.getIsin());
        assertEquals(elvl, actual.getElvl(), 0.01);
    }
}
