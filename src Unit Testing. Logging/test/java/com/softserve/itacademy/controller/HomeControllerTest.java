package com.softserve.itacademy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class HomeControllerTest {

    private final MockMvc mockMvc;

    @Autowired
    public HomeControllerTest (MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void createTaskGetTest () throws Exception {
        mockMvc.perform (MockMvcRequestBuilders.get("/home"))
                .andExpect (MockMvcResultMatchers.status().isOk())
                .andExpect (MockMvcResultMatchers.model().attributeExists("users"));
    }
}