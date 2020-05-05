package com.luxoft.assignment.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Isin {
    private UUID id;
    private String isin;

    public Isin(String isin) {
        this.id = UUID.randomUUID();
        this.isin = isin;
    }
}
