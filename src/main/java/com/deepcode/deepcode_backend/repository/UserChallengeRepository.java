package com.deepcode.deepcode_backend.repository;

import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.StatusChallenge;
import com.deepcode.deepcode_backend.entity.UserChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/// Repository para gestionar la relación UserChallenge (progreso de usuarios en retos)
/// Extiende JpaRepository para obtener métodos CRUD automáticos (save, findAll, delete, etc.)
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    /// Busca todos los usuarios que están haciendo un reto específico
    List<UserChallenge> findByChallengeId(ChallengesModel challengeId);

    /// Busca la relación específica entre un usuario y un reto (para verificar si existe)
    Optional<UserChallenge> findByUserIdAndChallengeId(UserModel user, ChallengesModel challengeId);

    /// Busca todos los retos de un usuario (completados y pendientes)
    List<UserChallenge> findByUserId(UserModel userId);

    /// Busca retos de un usuario filtrados por estado (PENDING o COMPLETED)
    List<UserChallenge> findByUserIdAndStatus(UserModel userId, StatusChallenge status);

    /// Verifica si ya existe una relación entre usuario y reto (devuelve true/false)
    boolean existsByUserIdAndChallengeId(UserModel userId, ChallengesModel challengeId);
}
