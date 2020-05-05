package com.luxoft.assignment.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Elvl {
    private UUID id;
    private UUID isin;
    private double elvl;

    public Elvl(UUID isin, double elvl) {
        this.id = UUID.randomUUID();
        this.isin = isin;
        this.elvl = elvl;
    }

    public Elvl(UUID id, UUID isin, double elvl) {
        this.id = id;
        this.isin = isin;
        this.elvl = elvl;
    }
}
