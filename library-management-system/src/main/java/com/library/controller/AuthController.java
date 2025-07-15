package com.library.controller;

import com.library.entity.User;
import com.library.service.AuthService;
import com.library.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    
    @Autowired
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }
    
    /**
     * POST /api/auth/login - Login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = authService.authenticate(request.getEmail(), request.getPassword());
            String token = jwtService.generateToken(user);
            
            return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email ou senha inválidos"));
        }
    }
    
    /**
     * POST /api/auth/register - Registro público
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
            );
            
            String token = jwtService.generateToken(user);
            
            return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/validate - Validar token
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            
            if (jwtService.isTokenValid(token)) {
                String email = jwtService.extractUsername(token);
                User user = authService.findByEmail(email);
                
                return ResponseEntity.ok(new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getIsActive()
                ));
            } else {
                return ResponseEntity.badRequest().body(new ErrorResponse("Token inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Token inválido"));
        }
    }
    
    // DTOs
    public static class LoginRequest {
        private String email;
        private String password;
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String phone;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
    
    public static class LoginResponse {
        private String token;
        private Long userId;
        private String name;
        private String email;
        private String role;
        
        public LoginResponse(String token, Long userId, String name, String email, Object role) {
            this.token = token;
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.role = role.toString();
        }
        
        // Getters
        public String getToken() { return token; }
        public Long getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
    
    public static class UserResponse {
        private Long id;
        private String name;
        private String email;
        private String role;
        private Boolean active;
        
        public UserResponse(Long id, String name, String email, Object role, Boolean active) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role.toString();
            this.active = active;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public Boolean getActive() { return active; }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() { return error; }
    }
}