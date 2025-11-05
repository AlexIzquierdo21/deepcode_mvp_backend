package com.deepcode.deepcode_backend.config;

import com.deepcode.deepcode_backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration /// Indica que esta clase define configuraciones de Spring (beans, etc.)
@EnableWebSecurity /// Habilita la seguridad web de Spring Security
public class SecurityConfig {
    /// Bean encargado de encriptar contraseñas con el algoritmo BCrypt.
    /// Es el que se usa al guardar o verificar contraseñas de usuarios.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /// Configuración principal de la seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter
    ) throws Exception {
        http.csrf(csrf -> csrf.disable()) /// Desactiva la protección CSRF (útil si la API se usa con JWT y no con sesiones)
                .authorizeHttpRequests(auth -> auth /// Define qué rutas son públicas y cuáles requieren autenticación
                        .requestMatchers("/auth/**").permitAll() /// Permite libre acceso a las rutas que empiecen por /auth/
                        .anyRequest().authenticated() /// Todas las demás requieren autenticación
                )
                /// Inserta el filtro JWT antes del filtro estándar de autenticación por usuario/contraseña
                /// Esto permite validar el token antes de que se intente cualquier autenticación tradicional.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        /// Devuelve la configuración de seguridad ya construida
        return http.build();
    }
}