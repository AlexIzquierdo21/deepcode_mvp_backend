package com.deepcode.deepcode_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/// Utilidad para generar, validar y extraer información de tokens JWT
@Component
public class JwtUtil {

    /// Clave secreta para firmar tokens (mínimo 32 caracteres para HS256)
    private final String SECRET_KEY = "miClaveSecretaSegura1234567890123456789012345678901234567890";

    /// Tiempo de expiración del token: 24 horas en milisegundos
    private final long EXPIRATION_TIME = 86400000;

    /// Genera un token JWT para un usuario autenticado
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)        /// Email del usuario (identificador principal)
                .setIssuedAt(new Date())  /// Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /// Fecha de expiración
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) /// Firma criptográfica con clave secreta
                .compact();  /// Construye y devuelve el token como String
    }

    /// Extrae el email (subject) almacenado en el token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    /// Extrae todos los claims (datos) del token parseándolo y verificando la firma
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)  /// Verifica que la firma sea válida con nuestra clave
                .parseClaimsJws(token)      /// Parsea el token
                .getBody();                 /// Obtiene los claims (datos internos)
    }

    /// Valida si el token es correcto: email coincide y no ha expirado
    public boolean validateToken(String token, String email) {
        return (extractEmail(token).equals(email) && !isTokenExpired(token));
    }

    /// Verifica si el token ha expirado comparando fecha de expiración con fecha actual
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}