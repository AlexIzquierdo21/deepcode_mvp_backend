package com.deepcode.deepcode_backend.controller;

import com.deepcode.deepcode_backend.dto.progress.MarkChallengeRequest;
import com.deepcode.deepcode_backend.entity.UserChallenge;
import com.deepcode.deepcode_backend.service.UserChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// Controlador REST para gestionar el progreso de usuarios en retos
@RestController
@RequestMapping("/progress") /// Ruta base: /progress
public class ProgressController {

    private final UserChallengeService userChallengeService;

    /// Constructor para inyectar UserChallengeService (Spring lo hace automáticamente)
    public ProgressController(UserChallengeService userChallengeService) {
        this.userChallengeService = userChallengeService;
    }

    /// POST /progress - Marca un reto como completado (requiere JWT)
    /// @param request recibe el JSON
    @PostMapping
    public ResponseEntity<UserChallenge> markAsCompleted
    (@RequestBody MarkChallengeRequest request) {
        /// Obtiene el email del usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Marca el reto como completado, actualizando o creando la relación UserChallenge
        UserChallenge userChallenge = userChallengeService.markAsCompleted(request.getChallengeId(), email, request.getNotes());
        return ResponseEntity.ok(userChallenge); /// 200 OK con el progreso actualizado
    }

    /// GET /progress/me - Obtiene el progreso completo del usuario autenticado (requiere JWT)
    @GetMapping("/me")
    public ResponseEntity<List<UserChallenge>> getUserProgress() {
        /// Obtiene el email del usuario autenticado desde el contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        /// Obtiene todos los retos (completados y pendientes) del usuario
        List<UserChallenge> progressList = userChallengeService.getUserProgress(email);
        return ResponseEntity.ok(progressList); /// 200 OK con la lista de progreso
    }
}











