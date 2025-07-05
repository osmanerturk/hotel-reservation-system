package com.hotel.apigateway.controller;


import com.hotel.common.dto.AuthResponse;
import com.hotel.common.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreCorrect() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("1234");

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .value(response -> {
                    assertThat(response.getToken()).isNotBlank();
                });
    }

    @Test
    void login_ShouldReturn401_WhenCredentialsAreInvalid() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("wrong-password");

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
