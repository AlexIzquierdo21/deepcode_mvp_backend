package com.deepcode.deepcode_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Genera getters, setters, toString, equals y hashCode automáticamente
@Data
/// Constructor con todos los parámetros
@AllArgsConstructor
/// Constructor vacío (requerido por Spring para deserializar JSON del login)
@NoArgsConstructor
public class LoginRequest {

    /// Email obligatorio y con formato válido
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    /// Contraseña obligatoria con mínimo 8 caracteres
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La longitud de la contraseña debe ser superior a 8 caracteres")
    private String password;
}
