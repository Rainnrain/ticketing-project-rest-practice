package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {


    @Autowired
    private MockMvc mvc;
    static String token;
    static UserDTO manager; // before all is static so this also needs to be static
    static ProjectDTO project;

    @BeforeAll()
    static void setUp() {
        token = "Bearer " + "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1WjV4b21kdXd1QUl6b2piZmYtb1FGTDBoVFAwY1ZON3Z4eGlBbEE3R200In0.eyJleHAiOjE2NzE0MjUyODYsImlhdCI6MTY3MTQwNzI4NiwianRpIjoiNTcxNmM2YTctZWY5ZS00YjY3LWE0MDAtZmUyOGI0ZTZkOGM3IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL2N5ZGVvLWRldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiJmNWIwZmYwMS00ZjU2LTRkZjMtYTEwZC01MzY1MzkyYzUwODMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ0aWNrZXRpbmctYXBwIiwic2Vzc2lvbl9zdGF0ZSI6IjJkNmUwMmE0LTMxZDQtNDI3MC04Y2JiLWI5OGIyMjU4MDVhYiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgxIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtY3lkZW8tZGV2IiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0aWNrZXRpbmctYXBwIjp7InJvbGVzIjpbIkFkbWluIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwic2lkIjoiMmQ2ZTAyYTQtMzFkNC00MjcwLThjYmItYjk4YjIyNTgwNWFiIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInByZWZlcnJlZF91c2VybmFtZSI6Im1pa2UifQ.Z9mdGB8a5HC2Gv-g_LgMz8Rh9HWmv6PSyRN1z9NTkRPj1l9cSQ0d7iQuix5epcJmhpF17gdw9LZV-Gzdvf9kB45YBo1Ve3CdWdQ2Bn_FSMrpWEyCabrLUq0p2rQI8cvgXol8tT0iH1Q9Cdzh7Tw3P8FVTukEhZm7spPBeWHSR8Bmnb0VwVpbZvFhQ-3FzfraiGRzpav8W-LmTEStHyZqjdiIAwnBdEhsP1S0GKvB3zFygy38eU62qTOrxM6WDkPFbLSjAjpvSJipdTQSvox0GX0E_NYwqb_AojxSr8KV3MroEcKBdC4el7qdoiChvhRxV0xJLTFlsOb7Ubi4Tq7Vyg";

        manager = new UserDTO(2L,
                "",
                "",
                "ozzy",
                "abc1",
                "",
                true,
                "",
                new RoleDTO(2L, "manager"),
                Gender.MALE);


        project = new ProjectDTO(
                "API Project",
                "PR001",
                manager,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                "Some details",
                Status.OPEN
        );
    }

    @Test
    void givenNoToken_getProjects() throws Exception {// testing edge cases
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenToken_getProjects() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/project")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].projectCode").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").exists())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").isString())
                .andExpect(jsonPath("$.data[0].assignedManager.userName").value("ozzy"));
    }

    @Test
    void givenToken_createProject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project is successfully created"));
    }

    @Test
    void givenToken_UpdateProject() throws Exception {
        project.setProjectName("API Project-2");
        mvc.perform(MockMvcRequestBuilders.put("/api/v1/project")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(toJsonString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("project updated"));
    }

    @Test
    void givenToken_DeleteProject() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/project/" + project.getProjectCode())
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("project deleted"));
    }

    private String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);// only puts the date
        objectMapper.registerModule(new JavaTimeModule());// 2020,12,18 -> 2022/12/18 changes to this format
        return objectMapper.writeValueAsString(obj);
    }

}