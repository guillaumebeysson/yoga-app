package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;


@TestPropertySource(locations="classpath:application-test.properties")
@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:cleanup.sql")
public class UserControllerIntegrationTest {

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
    public void getUserById_whenValidId_thenReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/1")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getUserById_whenInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserById_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/user/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteUser_whenInvalidId_thenReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/0")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteUser_whenIdIsNotANumber_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/id")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteUser_whenNoAuthorizationHeader_thenReturnsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void deleteUser_whenUnauthorizedUser_thenReturnsUnauthorized() throws Exception {
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder().username("yoga2@studio.com").build();
        Authentication authentication2 = new UsernamePasswordAuthenticationToken(userDetails2, null);
        String jwt2 = jwtUtils.generateJwtToken(authentication2);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/user/1")
                        .header("Authorization", "Bearer " + jwt2))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}

