package com.vkncode.project.controller;

import com.vkncode.project.domain.repository.ProjectRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void secretariatSave() throws Exception {
        MvcResult result;

        String json = "{\"secretary\": \"fabio\", \"underInvestigation\": true, \"destination\": \"INFRASTRUCTURE\",\"populationGrade\": 0}";

        result = mvc.perform(post("/secretariat")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        JSONObject secretariatSaved = new JSONObject(result.getResponse().getContentAsString());

        result = mvc.perform(get("/secretariat/" + secretariatSaved.get("id"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JSONObject secretariatSearched = new JSONObject(result.getResponse().getContentAsString());

        assertNotNull(secretariatSaved);
        assertNotNull(secretariatSearched);
        assertEquals(secretariatSaved.get("id"), secretariatSearched.get("id"));
        assertEquals(secretariatSaved.get("secretary"), secretariatSearched.get("secretary"));
        assertEquals(secretariatSaved.get("destination"), secretariatSearched.get("destination"));
    }
}
