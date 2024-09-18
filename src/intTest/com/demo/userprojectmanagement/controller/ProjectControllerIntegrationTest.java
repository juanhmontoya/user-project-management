package com.demo.userprojectmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.userprojectmanagement.model.Project;
import com.demo.userprojectmanagement.repository.ProjectRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProjectRepository projectRepository;

    @BeforeEach
    public void setUp() {
        projectRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetProject() throws Exception {
        String newProjectJson =
                "{\"name\": \"New Project\", \"description\": \"This is a new project\"}";

        mockMvc
                .perform(
                        post("/v1/projects").contentType(MediaType.APPLICATION_JSON).content(newProjectJson))
                .andExpect(status().isCreated());

        List<Project> projects = projectRepository.findAll();
        assertEquals(1, projects.size());
        assertEquals("New Project", projects.get(0).getName());
    }
}
