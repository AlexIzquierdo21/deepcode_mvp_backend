package com.deepcode.deepcode_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/// Genera automáticamente getters, setters, toString, equals y hashCode
@Data
/// Marca esta clase como entidad JPA
@Entity
/// Define el nombre de la tabla en la base de datos
@Table(name = "challenges")
public class ChallengesModel {

    /// Clave primaria con autoincremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Título del reto (obligatorio)
    @Column(nullable = false)
    private String title;

    /// Descripción del reto (obligatorio)
    @Column(nullable = false)
    private String description;

    /// Lenguaje de programación del reto, almacenado como String en BD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LanguageChallenge language;

    /// Nivel de dificultad del reto, almacenado como String en BD
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LevelChallenge level;

    /// Usuario que creó este reto (relación Muchos retos creados por un usuario)
    @ManyToOne
    @JoinColumn(nullable = false)
    private UserModel createdBy;

    /// Fecha de creación automática, no modificable después
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}












