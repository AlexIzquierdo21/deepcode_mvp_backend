package com.deepcode.deepcode_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/// Genera automáticamente getters, setters...
@Data
/// Marca esta clase como entidad JPA
@Entity
/// Define la tabla y evita que un usuario tenga el mismo reto duplicado
@Table(name = "user_challenges",
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"user_id", "challenge_id"}) )
public class UserChallenge {

    /// Clave primaria con autoincremento
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /// Usuario asociado a este progreso (Un usuario puede tener varios Challenge en progreso)
    @ManyToOne
    @JoinColumn(nullable = false)
    private UserModel userId;

    /// Reto asociado a este progreso (Un reto puede estar en progreso por varios usuarios)
    @ManyToOne
    @JoinColumn(nullable = false)
    private ChallengesModel challengeId;

    /// Estado actual del reto: PENDING o COMPLETED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusChallenge status;

    /// Notas opcionales del usuario sobre el reto
    @Column(nullable = true)
    private String notes;

    /// Fecha de completado (solo se llena cuando status = COMPLETED)
    @Column(nullable = true)
    private LocalDateTime completedAt;
}