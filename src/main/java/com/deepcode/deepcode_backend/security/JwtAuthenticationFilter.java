package com.deepcode.deepcode_backend.security;

import com.deepcode.deepcode_backend.entity.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.deepcode.deepcode_backend.service.UserService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/// Filtro de autenticación JWT que intercepta todas las peticiones HTTP
/// Se ejecuta una vez por petición para validar tokens JWT
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /// Constructor para inyectar dependencias
    public JwtAuthenticationFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /// Método principal que se ejecuta en cada petición HTTP
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        /// Extrae el header Authorization de la petición
        String authHeader = request.getHeader("Authorization");

        /// Si no hay header o no empieza con "Bearer ", continúa sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        /// Extrae el token JWT (quitando "Bearer " que son 7 caracteres)
        String token = authHeader.substring(7);

        try {
            /// Extrae el email del token JWT
            String email = jwtUtil.extractEmail(token);

            /// Busca el usuario en la base de datos por email
            Optional<UserModel> userModelOptional = userService.findByEmail(email);

            /// Si el usuario no existe, continúa sin autenticar
            if (userModelOptional.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            UserModel userModel = userModelOptional.get();

            /// Valida el token (verifica firma y expiración)
            boolean isValid = jwtUtil.validateToken(token, email);

            if (!isValid) {
                filterChain.doFilter(request, response);
                return;
            }


            /// Crea el objeto de autenticación de Spring Security
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(),
                            null, new ArrayList<>());

            /// Setea el usuario autenticado en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authToken);

            /// Continúa con la cadena de filtros
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            /// Si hay algún error (token inválido, expirado, etc.), continúa sin autenticar
            filterChain.doFilter(request, response);
            return;
        }
    }
}
