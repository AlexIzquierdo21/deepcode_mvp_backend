package com.deepcode.deepcode_backend.service;

import com.deepcode.deepcode_backend.dto.challenge.CreateChallengeRequest;
import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
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

    /// Obtiene todos los retos con filtros opcionales por lenguaje y nivel
    public List<ChallengesModel> getAllChallenges(LanguageChallenge language, LevelChallenge level) {
        /// Sin filtros: devuelve todos los retos
        if (language == null && level == null) {
            return challengesRepository.findAll();
            /// Filtro solo por lenguaje
        } else if (language != null && level == null) {
            return challengesRepository.findByLanguage(language);
            /// Filtro solo por nivel
        } else if (language == null && level != null) {
            return challengesRepository.findByLevel(level);
            /// Filtro por ambos: lenguaje y nivel
        } else {
            return challengesRepository.findByLanguageAndLevel(language, level);
        }
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
    public void deleteChallenge(Long id, String email) {
        ///  Obtener el reto (guárdalo en variable)
        ChallengesModel challengesModel = getChallengeById(id);
        /// Verificar si el usuario autenticado es el creador
        if(!challengesModel.getCreatedBy().getEmail().equals(email)) {
            throw new RuntimeException("No puedes eliminar el reto");
        }
        /// Si llegó aquí, es el creador → eliminar
        challengesRepository.deleteById(id);
    }
    /// Obtiene todos los retos creados por el usuario autenticado
    public List<ChallengesModel> getMyCreatedChallenges(String email) {
        // Buscar usuario por email (del JWT)
        Optional<UserModel> userOptional = userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        UserModel user = userOptional.get();

        // Buscar y devolver retos creados por este usuario
        return challengesRepository.findByCreatedBy(user);
    }
}















