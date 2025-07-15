package com.library.service;

import com.library.entity.User;
import com.library.entity.UserRole;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Criar novo usuário
     */
    public User createUser(String name, String email, String password, String phone, UserRole role) {
        // Verificar se email já existe
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }
        
        // Criptografar senha
        String encodedPassword = passwordEncoder.encode(password);
        
        // Criar usuário
        User user = new User(name, email, encodedPassword, phone, role);
        return userRepository.save(user);
    }
    
    /**
     * Buscar usuário por ID
     */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Buscar usuário por email (retorna Optional - para JWT)
     */
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Listar todos os usuários ativos
     */
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    /**
     * Listar usuários por papel
     */
    @Transactional(readOnly = true)
    public List<User> findUsersByRole(UserRole role) {
        return userRepository.findByRoleAndIsActiveTrue(role);
    }
    
    /**
     * Buscar usuários por nome
     */
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        return userRepository.findActiveUsersByNameContaining(name);
    }
    
    /**
     * Atualizar usuário
     */
    public User updateUser(Long id, String name, String phone) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        user.setName(name);
        user.setPhone(phone);
        
        return userRepository.save(user);
    }
    
    /**
     * Alterar senha
     */
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        
        userRepository.save(user);
    }
    
    /**
     * Ativar usuário
     */
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        user.activate();
        userRepository.save(user);
    }
    
    /**
     * Desativar usuário
     */
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        
        user.deactivate();
        userRepository.save(user);
    }
    
    /**
     * Contar usuários ativos
     */
    @Transactional(readOnly = true)
    public long countActiveUsers() {
        return userRepository.countByIsActiveTrue();
    }
    
    /**
     * Contar usuários por papel
     */
    @Transactional(readOnly = true)
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }
    
    /**
     * Verificar se usuário existe
     */
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
 * Estatísticas dos usuários
 */
@Transactional(readOnly = true)
public Map<String, Object> getUserStatistics() {
    Map<String, Object> stats = new HashMap<>();
    
    long totalUsers = userRepository.count();
    long activeUsers = userRepository.countByIsActiveTrue();
    long students = userRepository.countByRole(UserRole.STUDENT);
    long librarians = userRepository.countByRole(UserRole.LIBRARIAN);
    
    stats.put("total", totalUsers);
    stats.put("totalActive", activeUsers);
    stats.put("students", students);
    stats.put("librarians", librarians);
    stats.put("inactive", totalUsers - activeUsers);
    
    return stats;
}

/**
 * Usuários mais recentes
 */
  @Transactional(readOnly = true)
  public List<User> getRecentUsers(int limit) {
      return userRepository.findByIsActiveTrueOrderByCreatedAtDesc()
              .stream()
              .limit(limit)
              .toList();
  }
}