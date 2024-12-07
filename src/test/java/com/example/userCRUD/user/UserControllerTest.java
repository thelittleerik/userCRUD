package com.example.userCRUD.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import com.example.userCRUD.user.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId; // The ID of a user to perform tests on

    @BeforeEach
    void setUp() throws Exception {
        // Create a user before running tests that require an existing user
        UserDto userDto = new UserDto();
        userDto.setFirstname("John");
        userDto.setLastname("Doe");
        userDto.setAge(30);
        userDto.setHobbies(List.of("Reading", "Swimming"));
        userDto.setSmoker(false);
        userDto.setFavoriteFood("Pizza");

        String response = mockMvc.perform(post("/api/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract the user ID from the response and save it for later use in the tests
        userId = UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    void testCreateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Jane");
        userDto.setLastname("Smith");
        userDto.setAge(25);
        userDto.setHobbies(List.of("Cooking", "Jogging"));
        userDto.setSmoker(true);
        userDto.setFavoriteFood("Sushi");

        mockMvc.perform(post("/api/user/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("Jane"))
                .andExpect(jsonPath("$.lastname").value("Smith"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/user/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstname").isNotEmpty());
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void testGetUserByNonExistentId() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        mockMvc.perform(get("/api/user/users/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindUserByName() throws Exception {
        mockMvc.perform(get("/api/user/users/search")
                        .param("firstname", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstname("Jane");
        userDto.setLastname("Doe");
        userDto.setAge(35);
        userDto.setHobbies(List.of("Running", "Cycling"));
        userDto.setSmoker(true);
        userDto.setFavoriteFood("Burger");

        mockMvc.perform(put("/api/user/{id}/edit", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("Jane"))
                .andExpect(jsonPath("$.lastname").value("Doe"))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.hobbies[0]").value("Running"))
                .andExpect(jsonPath("$.smoker").value(true));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        UserDto userDto = new UserDto();
        userDto.setFirstname("Nonexistent");
        userDto.setLastname("User");

        mockMvc.perform(put("/api/user/{id}/edit", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", userId))
                .andExpect(status().isOk());

        // Try to get the user again after deletion
        mockMvc.perform(get("/api/user/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        mockMvc.perform(delete("/api/user/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}
