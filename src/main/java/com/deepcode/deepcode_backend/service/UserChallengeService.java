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

@Service
public class UserChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final UserService userService;
    private final ChallengeService challengeService;

    public UserChallengeService(UserChallengeRepository userChallengeRepository, UserService userService, ChallengeService challengeService) {
        this.userChallengeRepository = userChallengeRepository;
        this.userService = userService;
        this.challengeService = challengeService;
    }

    public UserChallenge markAsCompleted(Long challengeId, String email, String notes) {
        /// Buscar usuario
        Optional<UserModel> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel user = userOptional.get();

        /// Buscar reto
        ChallengesModel challenge = challengeService.getChallengeById(challengeId);

        /// Verificar si ya existe la relación
        Optional<UserChallenge> userChallengeOptional = userChallengeRepository.findByUserIdAndChallengeId(user, challenge);

        /// Decidir qué hacer
        if (userChallengeOptional.isPresent() && userChallengeOptional.get().getStatus() == StatusChallenge.COMPLETED) {
            /// Ya completado
            throw new RuntimeException("Ya completaste este reto");

        } else if (userChallengeOptional.isPresent()) {
            /// Existe pero está PENDING, actualizar a COMPLETED
            UserChallenge existing = userChallengeOptional.get();
            existing.setStatus(StatusChallenge.COMPLETED);
            existing.setCompletedAt(LocalDateTime.now());
            existing.setNotes(notes);
            return userChallengeRepository.save(existing);

        } else {
            /// No existe, crear nuevo
            UserChallenge newUserChallenge = new UserChallenge();
            newUserChallenge.setUserId(user);
            newUserChallenge.setChallengeId(challenge);
            newUserChallenge.setStatus(StatusChallenge.COMPLETED);
            newUserChallenge.setCompletedAt(LocalDateTime.now());
            newUserChallenge.setNotes(notes);
            return userChallengeRepository.save(newUserChallenge);
        }
    }

    public List<UserChallenge> getUserProgress(String email) {
        /// Buscar usuario
        Optional<UserModel> userOptional = userService.findByEmail(email);
        /// Validar que el user existe
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel user = userOptional.get();

        return userChallengeRepository.findByUserId(user);
    }
}










