package com.luxoft.assignment.controller;

import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.QuoteModel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private Manager manager;

    @Autowired
    public Controller(Manager manager) {
        this.manager = manager;
    }

    @GetMapping(value = "/elvls")
    public @ResponseBody CompletableFuture<ResponseEntity> get() {
        return manager.get().<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/elvls/{isin}")
    public @ResponseBody CompletableFuture<ResponseEntity> get(@PathVariable("isin") String isin) {
        return manager.get(isin).<ResponseEntity>thenApply(ResponseEntity::ok);
    }

    @PostMapping(value = "/quote")
    public void addQuote(@RequestBody QuoteModel quote, HttpServletResponse response) {
        try {
           if (!manager.add(quote).get()) {
               response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
           }
        } catch (InterruptedException | ExecutionException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static Function<Throwable, ResponseEntity<?>> handleFailure = throwable -> {
        LOGGER.error("Failed to add records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
