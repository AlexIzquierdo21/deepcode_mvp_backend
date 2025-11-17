package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/// Repository para gestionar usuarios
/// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, findById, delete, etc.)
public interface UserRepository extends JpaRepository<UserModel, Long> {

    /// Busca usuario por email IGNORANDO mayúsculas/minúsculas
    /// Se usa en login y en JwtAuthenticationFilter
    /// Permite encontrar el usuario aunque el JWT tenga el email en diferente capitalización
    @Query("SELECT u FROM UserModel u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserModel> findByEmail(@Param("email") String email);

    /// Verifica si existe un email (case-sensitive, método legacy)
    /// No se usa, mantener por compatibilidad
    boolean existsByEmail(String email);

    /// Busca usuario por email ignorando mayúsculas/minúsculas (método explícito)
    /// Mismo comportamiento que findByEmail(), mantener por claridad
    @Query("SELECT u FROM UserModel u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<UserModel> findByEmailIgnoreCase(@Param("email") String email);

    /// Verifica si existe un email ignorando mayúsculas/minúsculas
    /// Se usa en registro para evitar duplicados como Test@test.com y test@test.com
    @Query("SELECT COUNT(u) > 0 FROM UserModel u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
}