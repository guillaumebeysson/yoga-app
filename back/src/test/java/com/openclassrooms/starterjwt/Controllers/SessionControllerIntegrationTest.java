package com.openclassrooms.starterjwt.Controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class SessionControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private String jwt;

    @BeforeEach
    public void init() {
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void getSessionById_whenValidId_thenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/session/2")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getSessionById_whenInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/session/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getSessionById_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/session/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getAllSessions_whenAuthorized_thenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/session")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllSessions_whenUnauthorized_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/session"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void createSession_whenValidData_thenReturnsOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("SessionTest");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Description de la session de test");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/session")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateSession_whenValidData_thenReturnsOk() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Pilate");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Cours de pilate avec un expert du domaine");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/session/1")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateSession_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Pilate");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Cours de pilate avec un expert du domaine");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/session/id")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sessionDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteSession_whenInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/session/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteSession_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/session/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void participateInSession_whenValidData_thenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/session/1/participate/2")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void unParticipateInSession_whenValidData_thenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/session/1/participate/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void participateInSession_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/session/1/participate/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void unParticipateInSession_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/session/1/participate/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
