package com.deepcode.deepcode_backend.controller;

import com.deepcode.deepcode_backend.dto.challenge.CreateChallengeRequest;
import com.deepcode.deepcode_backend.entity.ChallengesModel;
import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import com.deepcode.deepcode_backend.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controlador REST para operaciones CRUD de retos de programación
@RestController
@RequestMapping("/challenges") /// Ruta base: /challenges
public class ChallengeController {

    private final ChallengeService challengeService;

    /// Constructor para inyectar ChallengeService (Spring lo hace automáticamente)
    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    /// POST /challenges - Crea un nuevo reto (requiere JWT)
    /// @param createChallengeRequest recibe el JSON del Front End
    @PostMapping
    public ResponseEntity<ChallengesModel> createChallenge
            (@RequestBody CreateChallengeRequest createChallengeRequest) {
        /// Obtiene el email del usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Crea el reto asociándolo al usuario autenticado
        ChallengesModel challenge = challengeService.createChallenge
                (createChallengeRequest, email);
        return ResponseEntity.ok(challenge); /// 200 OK con el reto creado
    }

    /// GET /challenges - Lista todos los retos con filtros opcionales (requiere JWT)
    /// Parámetros opcionales: ?language=PYTHON&level=BEGINNER
    @GetMapping
    public ResponseEntity<List<ChallengesModel>> getAllChallenges(
            @RequestParam(required = false) LanguageChallenge language,
            @RequestParam(required = false) LevelChallenge level) {
        /// Obtiene retos aplicando filtros si se proporcionan
        List<ChallengesModel> challenges = challengeService.getAllChallenges(language, level);
        return ResponseEntity.ok(challenges); /// 200 OK con la lista de retos
    }

    /// GET /challenges/{id} - Obtiene un reto específico por ID (requiere JWT)
    @GetMapping("/{id}")
    public ResponseEntity<ChallengesModel> getChallengeById(@PathVariable Long id) {
        /// Busca el reto por ID en la base de datos
        ChallengesModel challenge = challengeService.getChallengeById(id);
        return ResponseEntity.ok(challenge); /// 200 OK con el reto encontrado
    }
    /// DELETE /challenges/{id} - Elimina un reto por ID (requiere JWT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Elimina el reto de la base de datos
        challengeService.deleteChallenge(id, email);
        return ResponseEntity.noContent().build(); /// 204 No Content (eliminado exitosamente)
    }
}
















