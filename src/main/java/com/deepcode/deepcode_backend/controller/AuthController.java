package com.deepcode.deepcode_backend.controller;

import com.deepcode.deepcode_backend.dto.auth.AuthResponse;
import com.deepcode.deepcode_backend.dto.auth.LoginRequest;
import com.deepcode.deepcode_backend.dto.auth.RegisterRequest;
import com.deepcode.deepcode_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// Controlador REST para endpoints de autenticación
@RestController
@RequestMapping("/auth") /// Ruta base: /auth
public class AuthController {

    private final AuthService authService;

    /// Constructor para inyectar AuthService (Spring lo hace automáticamente)
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /// POST /auth/register - Registra un nuevo usuario y devuelve token JWT
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response); /// 200 OK con token y datos del usuario
    }

    /// POST /auth/login - Valida credenciales y devuelve token JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response); /// 200 OK con token y datos del usuario
    }
}