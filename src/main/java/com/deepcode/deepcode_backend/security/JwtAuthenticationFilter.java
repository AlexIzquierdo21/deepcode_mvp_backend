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


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;

    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try {
            String email = jwtUtil.extractEmail(token);
            Optional<UserModel> userModelOptional = userService.findByEmail(email);

            if (userModelOptional.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            UserModel userModel = userModelOptional.get();
            if (!jwtUtil.validateToken(token, email)) {
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(),
                            null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
    }
}
