package com.deepcode.deepcode_backend.dto.challenge;

import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/// DTO para recibir datos al crear un nuevo reto de programación
/// Genera getters, setters, toString, equals y hashCode automáticamente
@Data
/// Constructor vacío (requerido por Spring para deserializar JSON)
@NoArgsConstructor
/// Constructor con todos los parámetros
@AllArgsConstructor
public class CreateChallengeRequest {

    /// Título del reto (obligatorio)
    @NotBlank(message = "El título es obligatorio")
    private String title;

    /// Descripción detallada del reto (obligatorio)
    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    /// Lenguaje de programación del reto: PYTHON, HTML_CSS_JS, JAVA, KOTLIN (obligatorio)
    @NotNull(message = "El lenguaje es obligatorio")
    private LanguageChallenge language;

    /// Nivel de dificultad: BEGINNER, INTERMEDIATE (obligatorio)
    @NotNull(message = "El nivel es obligatorio")
    private LevelChallenge level;
}

