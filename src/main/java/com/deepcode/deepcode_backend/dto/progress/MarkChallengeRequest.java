package com.deepcode.deepcode_backend.dto.progress;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/// DTO para actualizar PROGRESS BAR al marcar un reto como completo
///
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkChallengeRequest {

    @NotNull(message = "El ID debe ser obligatorio")
    private Long challengeId;
    private String notes;
}
