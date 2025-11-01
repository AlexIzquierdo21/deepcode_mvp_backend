package com.deepcode.deepcode_backend.controller;

import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/// Controlador REST para operaciones relacionadas con usuarios
@RestController
@RequestMapping("/user") /// Ruta base: /user
public class UserController {

    private final UserService userService;

    /// Constructor para inyectar UserService (Spring lo hace automáticamente)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /// GET /user/me - Devuelve información del usuario autenticado (requiere JWT)
    @GetMapping("/me")
    public ResponseEntity<UserModel> getMe() {
        /// Obtiene el email del usuario autenticado desde el contexto de seguridad (seteado por JwtAuthenticationFilter)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Busca el usuario en la base de datos por email
        Optional<UserModel> userOptional = userService.findByEmail(email);
        /// Si el usuario no existe en la BD (caso raro, pero por seguridad)
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        /// Obtiene el usuario y lo devuelve en la respuesta
        UserModel user = userOptional.get();
        return ResponseEntity.ok(user); /// 200 OK con los datos del usuario
    }
}