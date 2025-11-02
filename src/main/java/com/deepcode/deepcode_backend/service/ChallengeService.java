package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.dto.challenge.CreateChallengeRequest;
import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.UserModel;
import com.deepcode.deepcode_backend.repository.ChallengesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// Servicio con lógica de negocio para gestionar retos de programación
@Service
public class ChallengeService {

    private final ChallengesRepository challengesRepository;
    private final UserService userService;

    /// Constructor para inyectar dependencias
    public ChallengeService(ChallengesRepository challengesRepository, UserService userService) {
        this.challengesRepository = challengesRepository;
        this.userService = userService;
    }

    /// Crea un nuevo reto asociado al usuario autenticado
    public ChallengesModel createChallenge(CreateChallengeRequest createChallengeRequest, String email) {
        // Busca el usuario por email (obtenido del JWT)
        Optional<UserModel> userOptional = userService.findByEmail(email);

        /// Verifica que el usuario existe en la BD
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado.");
        }
        UserModel user = userOptional.get();

        /// Crea una nueva instancia de ChallengesModel
        ChallengesModel challenge = new ChallengesModel();

        /// Copia los datos del DTO a la entidad
        challenge.setTitle(createChallengeRequest.getTitle());
        challenge.setDescription(createChallengeRequest.getDescription());
        challenge.setLanguage(createChallengeRequest.getLanguage());
        challenge.setLevel(createChallengeRequest.getLevel());
        challenge.setCreatedBy(user); // Asocia el reto al usuario autenticado

        /// Guarda en la BD y devuelve el reto creado (con id y createdAt generados)
        return challengesRepository.save(challenge);
    }

    /// Obtiene todos los retos de la base de datos
    public List<ChallengesModel> getAllChallenges() {
        return challengesRepository.findAll();
    }

    /// Busca un reto específico por ID
    public ChallengesModel getChallengeById(Long id) {
        /// Busca el reto en la BD
        Optional<ChallengesModel> challengesOptional = challengesRepository.findById(id);

        /// Si no existe, lanza excepción
        if (challengesOptional.isEmpty()) {
            throw new RuntimeException("Reto no encontrado");
        }
        /// Devuelve el reto encontrado
        return challengesOptional.get();
    }
    public void deleteChallenge(Long id) {
        getChallengeById(id);
        challengesRepository.deleteById(id);
    }
}















