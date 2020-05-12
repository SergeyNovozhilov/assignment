package com.luxoft.assignment.controller;

import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.Elvl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ElvlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElvlController.class);
    private Manager manager;

    @Autowired
    public ElvlController(Manager manager) {
        this.manager = manager;
    }

    @GetMapping(value = "/elvls")
    public Collection<Elvl> get() {
        return manager.get();
    }

    @GetMapping(value = "/elvls/{isin}")
    public Elvl get(@PathVariable("isin") String isin) {
        return manager.get(isin);
    }
}
