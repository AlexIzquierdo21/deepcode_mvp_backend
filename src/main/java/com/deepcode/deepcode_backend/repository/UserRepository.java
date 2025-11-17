package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/// Repository para gestionar usuarios
/// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, findById, delete, etc.)
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    boolean existsByEmail(String email);

    /// Busca usuario por email ignorando mayúsculas/minúsculas
    @Query("SELECT u FROM UserModel u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserModel> findByEmailIgnoreCase(@Param("email") String email);

    /// Verifica si existe un email ignorando mayúsculas/minúsculas
    @Query("SELECT COUNT(u) > 0 FROM UserModel u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
}
