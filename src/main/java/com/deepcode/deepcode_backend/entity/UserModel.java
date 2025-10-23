package com.deepcode.deepcode_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/// Genera automáticamente getters, setters, toString, equals y hashCode
@Data
/// Marca esta clase como entidad JPA
@Entity
/// Define el nombre de la tabla en la base de datos
@Table(name = "users")
public class UserModel {
    /// Clave primaria con autoincremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Nombre de usuario único (obligatorio)
    @Column(nullable = false, unique = true)
    private String username;

    /// Email único (obligatorio)
    @Column(nullable = false, unique = true)
    private String email;

    /// Contraseña hasheada (solo se recibe en requests, nunca se envía en responses)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /// Fecha de registro automática, no modificable después
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}