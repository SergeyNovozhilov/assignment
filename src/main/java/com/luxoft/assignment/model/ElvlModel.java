package com.luxoft.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElvlModel {
    private String isin;
    private double elvl;

    public ElvlModel(ElvlModel elvlModel) {
    }
}
