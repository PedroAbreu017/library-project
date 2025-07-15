package com.library.config;

import com.library.service.JwtService;
import com.library.service.UserService;
import com.library.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Debug log
        System.out.println("JWT Filter executando para: " + request.getRequestURI());
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Debug log
        System.out.println("Authorization header: " + authHeader);

        // Se não há header Authorization ou não começa com "Bearer ", pular
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Sem token JWT válido, continuando...");
            filterChain.doFilter(request, response);
            return;
        }

        // Extrair o token JWT
        jwt = authHeader.substring(7);
        System.out.println("Token extraído: " + jwt.substring(0, 20) + "...");
        
        try {
            userEmail = jwtService.extractUsername(jwt);
            System.out.println("Email extraído do token: " + userEmail);

            // Se tem email e não há autenticação no contexto
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Buscar usuário
                Optional<User> userOptional = userService.findByEmail(userEmail);
                System.out.println("Usuário encontrado: " + userOptional.isPresent());
                
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    
                    // Validar token
                    boolean isValidToken = jwtService.isTokenValid(jwt, user);
                    System.out.println("Token válido: " + isValidToken);
                    
                    if (isValidToken) {
                        
                        // Criar autenticação
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );
                        
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        
                        // Definir autenticação no contexto
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("Usuário autenticado com sucesso: " + user.getEmail());
                    }
                }
            }
        } catch (Exception e) {
            // Token inválido, continuar sem autenticação
            System.out.println("Erro ao processar JWT: " + e.getMessage());
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}