package com.library.controller;

import com.library.entity.User;
import com.library.entity.UserRole;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * GET /api/users - Listar todos os usuários ativos
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/{id} - Buscar usuário por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/users - Criar novo usuário
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getRole() != null ? request.getRole() : UserRole.STUDENT
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * PUT /api/users/{id} - Atualizar usuário
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        try {
            User user = userService.updateUser(id, request.getName(), request.getPhone());
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * DELETE /api/users/{id} - Desativar usuário
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(new SuccessResponse("Usuário desativado com sucesso"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * GET /api/users/search?name=... - Buscar usuários por nome
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        List<User> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/users/count - Contar usuários ativos
     */
    @GetMapping("/count")
    public ResponseEntity<CountResponse> countUsers() {
        long activeUsers = userService.countActiveUsers();
        long students = userService.countUsersByRole(UserRole.STUDENT);
        long librarians = userService.countUsersByRole(UserRole.LIBRARIAN);
        
        return ResponseEntity.ok(new CountResponse(activeUsers, students, librarians));
    }
    
    // DTOs
    public static class CreateUserRequest {
        private String name;
        private String email;
        private String password;
        private String phone;
        private UserRole role;
        
        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }
    
    public static class UpdateUserRequest {
        private String name;
        private String phone;
        
        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
    
    public static class SuccessResponse {
        private String message;
        
        public SuccessResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class CountResponse {
        private long totalActive;
        private long students;
        private long librarians;
        
        public CountResponse(long totalActive, long students, long librarians) {
            this.totalActive = totalActive;
            this.students = students;
            this.librarians = librarians;
        }
        
        // Getters e Setters
        public long getTotalActive() { return totalActive; }
        public void setTotalActive(long totalActive) { this.totalActive = totalActive; }
        
        public long getStudents() { return students; }
        public void setStudents(long students) { this.students = students; }
        
        public long getLibrarians() { return librarians; }
        public void setLibrarians(long librarians) { this.librarians = librarians; }
    }
}