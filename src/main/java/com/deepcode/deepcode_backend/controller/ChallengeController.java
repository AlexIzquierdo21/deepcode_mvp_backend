package com.deepcode.deepcode_backend.controller;

import com.deepcode.deepcode_backend.dto.challenge.CreateChallengeRequest;
import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para operaciones CRUD de retos de programación
@RestController
@RequestMapping("/challenges") // Ruta base: /challenges
public class ChallengeController {

    private final ChallengeService challengeService;

    /// Constructor para inyectar ChallengeService (Spring lo hace automáticamente)
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    /// POST /challenges - Crea un nuevo reto (requiere JWT)
    @PostMapping
    public ResponseEntity<ChallengesModel> createChallenge(@RequestBody CreateChallengeRequest createChallengeRequest) {
        /// Obtiene el email del usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Crea el reto asociándolo al usuario autenticado
        ChallengesModel challenge = challengeService.createChallenge(createChallengeRequest, email);
        return ResponseEntity.ok(challenge); // 200 OK con el reto creado
    }

    /// GET /challenges - Lista todos los retos (requiere JWT)
    @GetMapping
    public ResponseEntity<List<ChallengesModel>> getAllChallenges() {
        /// Obtiene todos los retos de la base de datos
        List<ChallengesModel> challenges = challengeService.getAllChallenges();
        return ResponseEntity.ok(challenges); // 200 OK con la lista de retos
    }

    /// GET /challenges/{id} - Obtiene un reto específico por ID (requiere JWT)
    @GetMapping("/{id}")
    public ResponseEntity<ChallengesModel> getChallengeById(@PathVariable Long id) {
        /// Busca el reto por ID en la base de datos
        ChallengesModel challenge = challengeService.getChallengeById(id);
        return ResponseEntity.ok(challenge); /// 200 OK con el reto encontrado
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);

        return ResponseEntity.noContent().build();
    }
}


















