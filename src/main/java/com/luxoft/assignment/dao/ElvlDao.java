package com.luxoft.assignment.dao;

import com.luxoft.assignment.domain.Elvl;
import com.luxoft.assignment.domain.Isin;
import com.luxoft.assignment.model.ElvlModel;

import java.util.List;

public interface ElvlDao {
    void add(Elvl elvl);
    ElvlModel get(String isin);
    List<ElvlModel> get();
    void add(Isin isin);
    void update(ElvlModel elvlModel);
}
