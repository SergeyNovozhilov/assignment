package com.luxoft.assignment.controller;

import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.Elvl;
import com.luxoft.assignment.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private Manager manager;

    @Autowired
    public Controller(Manager manager) {
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

    @PostMapping(value = "/quote")
    public void addQuote(@RequestBody Quote quote, HttpServletResponse response) {
        manager.add(quote).exceptionally(throwable -> {
            LOGGER.error("Failed to add records: {}", throwable);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return false;
        });
    }
}
