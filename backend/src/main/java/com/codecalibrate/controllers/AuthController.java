package com.codecalibrate.controllers;

import com.codecalibrate.domain.UserService;
import com.codecalibrate.dto.RegisterUserRequest;
import com.codecalibrate.dto.RegisterUserResponse;
import com.codecalibrate.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codecalibrate.domain.JwtService;
import com.codecalibrate.dto.LoginRequest;
import com.codecalibrate.dto.LoginResponse;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        User user = userService.register(request);
        RegisterUserResponse response = new RegisterUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        User user = userService.authenticate(request);
        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                token
        );

        return ResponseEntity.ok(response);
    }
}
