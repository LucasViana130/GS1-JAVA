package br.com.fiap.agroorbit.controllers;

import br.com.fiap.agroorbit.dtos.request.LoginRequest;
import br.com.fiap.agroorbit.dtos.request.RegisterRequest;
import br.com.fiap.agroorbit.dtos.response.AuthResponse;
import br.com.fiap.agroorbit.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        AuthResponse response = service.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        AuthResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }
}