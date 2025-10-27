package com.deepcode.deepcode_backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Genera getters, setters, toString, equals y hashCode automáticamente
@Data
/// Constructor con todos los parámetros (útil para crear la respuesta con token y datos del usuario)
@AllArgsConstructor
/// Constructor vacío
@NoArgsConstructor
public class AuthResponse {

    /// Token JWT que el frontend guardará para autenticarse en futuras peticiones
    private String token;

    /// Username del usuario autenticado
    private String username;

    /// Email del usuario autenticado
    private String email;
}