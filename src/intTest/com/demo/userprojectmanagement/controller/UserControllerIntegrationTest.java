package com.demo.userprojectmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.demo.userprojectmanagement.model.User;
import com.demo.userprojectmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        String newUserJson = "{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}";

        mockMvc
                .perform(post("/v1/users").contentType(MediaType.APPLICATION_JSON).content(newUserJson))
                .andExpect(status().isCreated());

        User user = userRepository.findAll().get(0);
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    public void testFindById() throws Exception {
        User user = new User();
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user = userRepository.save(user);

        mockMvc
                .perform(get("/v1/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old.email@example.com");
        user = userRepository.save(user);

        String updatedUserJson =
                String.format(
                        "{\"id\": %d, \"name\": \"Updated Name\", \"email\": \"updated.email@example.com\"}",
                        user.getId());

        mockMvc
                .perform(put("/v1/users").contentType(MediaType.APPLICATION_JSON).content(updatedUserJson))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated.email@example.com", updatedUser.getEmail());
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("User to Delete");
        user.setEmail("delete.me@example.com");
        user = userRepository.save(user);

        mockMvc.perform(delete("/v1/users/{id}", user.getId())).andExpect(status().isNoContent());

        assertEquals(0, userRepository.count());
    }
}
