package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.dto.auth.RegisterRequest;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /// Constructor del servicio de usuarios
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /// Registra un nuevo usuario desde DTO
    public UserModel registerUser(RegisterRequest request) {
        /// 1. Verificar si YA existe (evitar duplicado)
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado.");
        }
        /// 2. Crear NUEVO usuario
        UserModel user = new UserModel();
        /// 3. Copiar datos del DTO
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        /// 4. Guardar en BD
        return userRepository.save(user);
    }

    /// Obtener todos los usuarios de la BD
    public ArrayList<UserModel> getUsers() {
        return (ArrayList<UserModel>) userRepository.findAll();
    }

    /// Buscar usuario por email
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /// Verifica si el email ya tiene cuenta vinculada
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}