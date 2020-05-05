package com.luxoft.assignment.dao;

import com.luxoft.assignment.domain.Elvl;
import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.mapper.ElvlMapper;
import com.luxoft.assignment.model.ElvlModel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ElvlDaoImpl implements ElvlDao {
    private NamedParameterJdbcOperations jdbc;

    public ElvlDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void add(Elvl elvl) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", elvl.getId().toString());
        params.put("isin", elvl.getIsin());
        params.put("elvl", elvl.getElvl());

        jdbc.update("insert into ELVLS (id, isin, elvl) " +
                "values (:id, :isin, :elvl)", params);
    }

    @Override
    public ElvlModel get(String isin) {
        Map<String, String> params = Collections.singletonMap("isin", isin);
        try {
            return jdbc.queryForObject("select i.isin, e.elvl from ISINS i, ELVLS e \n" +
                            "where i.isin = :isin\n" +
                            "and i.id = e.isin;",
                    params, new ElvlMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ElvlModel> get() {
        try {
            return jdbc.query("select i.isin, e.elvl from ISINS i, ELVLS e \n" +
                    "where i.id = e.isin;", new ElvlMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void add(Isin isin) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", isin.getId().toString());
        params.put("isin", isin.getIsin());

        jdbc.update("insert into ISINS (id, isin) " +
                "values (:id, :isin)", params);
    }

    @Override
    public void update(ElvlModel elvlModel) {
        Map<String, Object> params = new HashMap<>();
        params.put("isin", elvlModel.getIsin());
        params.put("elvl", elvlModel.getElvl());

        jdbc.update("update ELVLS " +
                "set elvl = :elvl " +
                "where isin in (select id from ISINS where isin = :isin) ", params);
    }
}
