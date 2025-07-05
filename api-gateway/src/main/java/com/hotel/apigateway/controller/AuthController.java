package com.hotel.apigateway.controller;


import com.hotel.common.dto.AuthResponse;
import com.hotel.common.dto.LoginRequest;
import com.hotel.common.security.JwtTokenUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final Map<String, String> dummyRoles = Map.of(
            "user1", "ROLE_USER",
            "user2", "ROLE_USER",
            "user3", "ROLE_USER",
            "user4", "ROLE_USER",
            "admin", "ROLE_ADMIN"
    );


    private final Map<String, String> dummyUsers = Map.of(
            "user1", "1234",
            "user2", "1234",
            "user3", "1234",
            "user4", "1234",
            "admin", "admin"
    );

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        String storedPassword = dummyUsers.get(request.getUsername());

        if (storedPassword != null && storedPassword.equals(request.getPassword())) {
            String role = dummyRoles.getOrDefault(request.getUsername(), "ROLE_USER");
            String token = JwtTokenUtils.generateToken(request.getUsername(), role);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }

}

