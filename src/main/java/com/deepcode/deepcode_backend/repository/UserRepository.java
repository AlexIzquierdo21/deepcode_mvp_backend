package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository para gestionar usuarios
// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, findById, delete, etc.)
public interface UserRepository extends JpaRepository<UserModel, Long> {

    // Busca un usuario por su email (útil para login y validaciones)
    Optional<UserModel> findByEmail(String email);

    // Verifica si ya existe un usuario con ese email (para evitar duplicados en registro)
    boolean existsByEmail(String email);
}
