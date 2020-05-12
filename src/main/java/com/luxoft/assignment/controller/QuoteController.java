package com.luxoft.assignment.controller;

import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class QuoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteController.class);
    private Manager manager;

    @Autowired
    public QuoteController(Manager manager) {
        this.manager = manager;
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
