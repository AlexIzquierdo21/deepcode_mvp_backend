package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.dto.auth.RegisterRequest;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

/// Servicio con lógica de negocio para gestionar usuarios
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /// Constructor del servicio de usuarios
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /// Registra un nuevo usuario desde DTO
    /// Guarda el email con su formato original (mayúsculas/minúsculas como lo escribió el usuario)
    /// pero valida duplicados comparando en minúsculas (case-insensitive)
    public UserModel registerUser(RegisterRequest request) {
        /// Verificar si YA existe un email igual (ignora mayúsculas/minúsculas)
        /// Esto evita registros duplicados como: Alex@test.com, alex@test.com, ALEX@TEST.COM
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }

        /// Crear NUEVO usuario
        UserModel user = new UserModel();

        /// Copiar datos del DTO
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());  // Guarda el email ORIGINAL (con mayúsculas como lo escribió)
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        /// Guardar en BD y devolver usuario guardado
        return userRepository.save(user);
    }

    /// Obtener todos los usuarios de la BD
    public ArrayList<UserModel> getUsers() {
        return (ArrayList<UserModel>) userRepository.findAll();
    }

    /// Buscar usuario por email EXACTO (case-sensitive)
    /// Para login: el usuario debe escribir el email EXACTAMENTE como lo registró
    /// Si se registró como "Alex@Test.COM", debe hacer login con "Alex@Test.COM" (no "alex@test.com")
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);  // Case-sensitive (exacto)
    }

    /// Verifica si el email ya tiene cuenta vinculada (ignora mayúsculas/minúsculas)
    /// Para validaciones de duplicado en registro
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
}