package com.deepcode.deepcode_backend.service;


import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.StatusChallenge;
import com.deepcode.deepcode_backend.entity.UserChallenge;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.UserChallengeRepository;
import com.deepcode.deepcode_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final UserService userService;
    private final ChallengeService challengeService;
    private final UserRepository userRepository;

    public UserChallengeService(UserChallengeRepository userChallengeRepository, UserService userService, ChallengeService challengeService, UserRepository userRepository) {
        this.userChallengeRepository = userChallengeRepository;
        this.userService = userService;
        this.challengeService = challengeService;
        this.userRepository = userRepository;
    }

    public UserChallenge markAsCompleted(Long challengeId, String email, String notes) {
        Optional<UserModel> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel userModel = userOptional.get();

        ChallengesModel challengesModel = challengeService.getChallengeById(challengeId);

        Optional<UserChallenge> userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userModel, challengesModel);

        if (userChallenge.isPresent() && userChallenge.get().getStatus() == StatusChallenge.COMPLETED) {
            throw new RuntimeException("Ya completaste este reto");
        } else if (userChallenge.isPresent()) {
            throw new RuntimeException("Reto pendiente");
        } else {
            throw new RuntimeException("El reto no existe");
        }
        ///UserChallenge userChallenge = userOptional.get();
        return userChallengeRepository.save(userChallenge);
    }
}










