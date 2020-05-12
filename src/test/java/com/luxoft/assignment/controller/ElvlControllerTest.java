package com.luxoft.assignment.controller;

import com.luxoft.assignment.manager.Manager;
import com.luxoft.assignment.model.Elvl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ElvlControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Manager manager;

    @Test
    public void getAllElvlsTest() throws Exception {
        when(manager.get()).thenReturn(Collections.singletonList(new Elvl("RU000A0JX0J2", new BigDecimal(100.6))));
        this.mockMvc.perform(get("/elvls")).andDo(print()).andExpect(status().isOk());
        verify(manager).get();
    }

    @Test
    public void getElvlByIsinTest() throws Exception {
        when(manager.get(anyString())).thenReturn(new Elvl("RU000A0JX0J2", new BigDecimal(100.6)));
        this.mockMvc.perform(get("/elvls/RU000A0JX0J2")).andDo(print()).andExpect(status().isOk());
        verify(manager).get("RU000A0JX0J2");
    }
}
