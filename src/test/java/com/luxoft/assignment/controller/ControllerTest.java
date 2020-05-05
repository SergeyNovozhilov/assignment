package com.luxoft.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.QuoteModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Manager manager;

    @Test
    public void getAllElvlsTest() throws Exception {
        when(manager.get()).thenReturn(new CompletableFuture<>());
        this.mockMvc.perform(get("/elvls")).andDo(print()).andExpect(status().isOk());
        verify(manager).get();
    }

    @Test
    public void getElvlByIsinTest() throws Exception {
        when(manager.get(anyString())).thenReturn(new CompletableFuture<>());
        this.mockMvc.perform(get("/elvls/RU000A0JX0J2")).andDo(print()).andExpect(status().isOk());
        verify(manager).get("RU000A0JX0J2");
    }

    @Test
    public void addQuote() throws Exception {
        CompletableFuture<Boolean> future
                = CompletableFuture.supplyAsync(() -> true);
        when(manager.add(any(QuoteModel.class))).thenReturn(future);
        this.mockMvc.perform(post("/quote").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new QuoteModel("RU000A0JX0J2", 100.2, 100.5)))).andDo(print()).andExpect(status().isOk());
        verify(manager).add(isA(QuoteModel.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
