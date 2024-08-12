package com.openclassrooms.starterjwt.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class AuthControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void logIn_whenCredentialsAreValid_thenReturnsOk() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void logIn_whenCredentialsAreInvalid_thenReturnsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("fakePassword");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void register_whenDataIsValid_thenReturnsOk() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(UUID.randomUUID() + "@studio.com");
        signupRequest.setLastName("newLastName");
        signupRequest.setFirstName("newFirstName");
        signupRequest.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Testing equals, hashCode, toString, and canEqual
        SignupRequest anotherSignupRequest = new SignupRequest();
        anotherSignupRequest.setEmail(signupRequest.getEmail());
        anotherSignupRequest.setLastName(signupRequest.getLastName());
        anotherSignupRequest.setFirstName(signupRequest.getFirstName());
        anotherSignupRequest.setPassword(signupRequest.getPassword());

        assertEquals(signupRequest, anotherSignupRequest);
        assertEquals(signupRequest.hashCode(), anotherSignupRequest.hashCode());
        assertEquals(signupRequest.toString(), anotherSignupRequest.toString());
        assertEquals(signupRequest, anotherSignupRequest);
    }

    @Test
    public void register_whenEmailIsAlreadyTaken_thenReturnsBadRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setLastName("newLastName");
        signupRequest.setFirstName("newFirstName");
        signupRequest.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}
