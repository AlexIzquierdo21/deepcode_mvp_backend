package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.StatusChallenge;
import com.deepcode.deepcode_backend.entity.UserChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.UserChallengeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/// Servicio con lógica de negocio para gestionar el progreso de usuarios en retos
@Service
public class UserChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final UserService userService;
    private final ChallengeService challengeService;

    /// Constructor para inyectar dependencias
    public UserChallengeService(UserChallengeRepository userChallengeRepository, UserService userService, ChallengeService challengeService) {
        this.userChallengeRepository = userChallengeRepository;
        this.userService = userService;
        this.challengeService = challengeService;
    }

    /// Marca un reto como completado por el usuario autenticado
    /// Maneja 3 casos: ya completado, actualizar de PENDING a COMPLETED, o crear nuevo
    public UserChallenge markAsCompleted(Long challengeId, String email, String notes) {
        /// Busca el usuario por email (obtenido del JWT)
        Optional<UserModel> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel user = userOptional.get();

        /// Busca el reto por ID (lanza excepción si no existe)
        ChallengesModel challenge = challengeService.getChallengeById(challengeId);

        /// Verifica si ya existe una relación UserChallenge para este usuario y reto
        Optional<UserChallenge> userChallengeOptional = userChallengeRepository.findByUserIdAndChallengeId(user, challenge);

        /// Caso A: Ya completado previamente
        if (userChallengeOptional.isPresent() && userChallengeOptional.get().getStatus() == StatusChallenge.COMPLETED) {
            throw new RuntimeException("Ya completaste este reto");

            /// Caso B: Existe pero está PENDING, actualizar a COMPLETED
        } else if (userChallengeOptional.isPresent()) {
            UserChallenge existing = userChallengeOptional.get();
            existing.setStatus(StatusChallenge.COMPLETED);
            existing.setCompletedAt(LocalDateTime.now());
            existing.setNotes(notes);
            return userChallengeRepository.save(existing);

            /// Caso C: No existe relación, crear nueva con status COMPLETED
        } else {
            UserChallenge newUserChallenge = new UserChallenge();
            newUserChallenge.setUserId(user);
            newUserChallenge.setChallengeId(challenge);
            newUserChallenge.setStatus(StatusChallenge.COMPLETED);
            newUserChallenge.setCompletedAt(LocalDateTime.now());
            newUserChallenge.setNotes(notes);
            return userChallengeRepository.save(newUserChallenge);
        }
    }

    /// Obtiene el progreso (retos completados y pendientes) del usuario autenticado
    public List<UserChallenge> getUserProgress(String email) {
        /// Busca el usuario por email
        Optional<UserModel> userOptional = userService.findByEmail(email);

        /// Valida que el usuario existe
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel user = userOptional.get();

        /// Busca y devuelve todos los UserChallenge asociados a este usuario
        return userChallengeRepository.findByUserId(user);
    }
}









