package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.dto.auth.AuthResponse;
import com.deepcode.deepcode_backend.dto.auth.LoginRequest;
import com.deepcode.deepcode_backend.dto.auth.RegisterRequest;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.deepcode.deepcode_backend.service.UserService;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    /// Constructor para inyectar dependencias
    public AuthService(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    /// Registra un nuevo usuario y devuelve token JWT
    public AuthResponse register(RegisterRequest request) {
        /// Registrar usuario (guarda el resultado en una variable)
        UserModel user = userService.registerUser(request);
        /// Generar token JWT con el email del usuario
        String token = jwtUtil.generateToken(user.getEmail());
        /// Crea y devuelve AuthResponse con el token y datos del usuario
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }

    /// Valida credenciales y devuelve token JWT
    public AuthResponse login(LoginRequest request) {
        /// Buscar usuario por email
        Optional<UserModel> userOptional = userService.findByEmail(request.getEmail());
        /// Si NO existe → error
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Credenciales inválidas");
        }
        UserModel user = userOptional.get();

        /// Verificar si el password del request coincide con el password hasheado de la BD
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        /// Generar token JWT
        String token = jwtUtil.generateToken(user.getEmail());
        /// Devolver AuthResponse
        return new AuthResponse(token, user.getUsername(), user.getEmail());
    }
}