package com.library.service;

import com.library.entity.User;
import com.library.entity.UserRole;
import com.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Autenticar usuário (login)
     */
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos"));
        
        if (!user.getIsActive()) {
            throw new BadCredentialsException("Usuário desativado");
        }
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }
        
        return user;
    }
    
    /**
     * Registrar novo usuário
     */
    public User register(String name, String email, String password, String phone) {
        // Verificar se email já existe
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        // Validar dados
        validateUserData(name, email, password, phone);
        
        // Criar usuário
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, email, encodedPassword, phone, UserRole.STUDENT);
        
        return userRepository.save(user);
    }
    
    /**
     * Buscar usuário por email
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
    
    /**
     * Alterar senha
     */
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = authenticate(email, currentPassword);
        
        validatePassword(newPassword);
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        
        userRepository.save(user);
    }
    
    /**
     * Verificar se usuário existe
     */
    @Transactional(readOnly = true)
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Validar dados do usuário
     */
    private void validateUserData(String name, String email, String password, String phone) {
        if (name == null || name.trim().length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        validatePassword(password);
        
        if (phone == null || phone.trim().length() < 10) {
            throw new IllegalArgumentException("Telefone deve ter pelo menos 10 caracteres");
        }
    }
    
    /**
     * Validar senha
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres");
        }
        
        // Opcionalmente, adicionar mais validações:
        // - Deve conter pelo menos uma letra maiúscula
        // - Deve conter pelo menos um número
        // - Deve conter pelo menos um caractere especial
    }
}