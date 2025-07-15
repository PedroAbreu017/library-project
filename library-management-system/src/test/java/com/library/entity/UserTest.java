package com.library.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User Entity Tests")
class UserTest {
    
    private User user;
    private final String VALID_NAME = "João Silva";
    private final String VALID_EMAIL = "joao@email.com";
    private final String VALID_PASSWORD = "senha123";
    private final String VALID_PHONE = "11999999999";
    
    @BeforeEach
    void setUp() {
        user = new User(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_PHONE);
    }
    
    @Test
    @DisplayName("Deve criar usuário com construtor padrão")
    void shouldCreateUserWithDefaultConstructor() {
        User newUser = new User();
        
        assertNotNull(newUser);
        assertNull(newUser.getId());
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
    }
    
    @Test
    @DisplayName("Deve criar usuário com dados válidos")
    void shouldCreateUserWithValidData() {
        assertNotNull(user);
        assertEquals(VALID_NAME, user.getName());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertEquals(VALID_PHONE, user.getPhone());
        assertEquals(UserRole.STUDENT, user.getRole());
        assertTrue(user.getIsActive());
    }
    
    @Test
    @DisplayName("Deve criar usuário com papel específico")
    void shouldCreateUserWithSpecificRole() {
        User librarian = new User(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_PHONE, UserRole.LIBRARIAN);
        
        assertEquals(UserRole.LIBRARIAN, librarian.getRole());
        assertTrue(librarian.isLibrarian());
        assertFalse(librarian.isStudent());
    }
    
    @Test
    @DisplayName("Deve verificar papel de estudante")
    void shouldVerifyStudentRole() {
        assertTrue(user.isStudent());
        assertFalse(user.isLibrarian());
    }
    
    @Test
    @DisplayName("Deve ativar e desativar usuário")
    void shouldActivateAndDeactivateUser() {
        // Inicialmente ativo
        assertTrue(user.getIsActive());
        
        // Desativar
        user.deactivate();
        assertFalse(user.getIsActive());
        
        // Ativar novamente
        user.activate();
        assertTrue(user.getIsActive());
    }
    
    @Test
    @DisplayName("Deve implementar UserDetails corretamente")
    void shouldImplementUserDetailsCorrectly() {
        // Username deve ser o email
        assertEquals(VALID_EMAIL, user.getUsername());
        
        // Authorities deve conter ROLE_STUDENT
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT")));
        
        // Conta não expirada
        assertTrue(user.isAccountNonExpired());
        
        // Credenciais não expiradas
        assertTrue(user.isCredentialsNonExpired());
        
        // Conta não bloqueada (depende de isActive)
        assertTrue(user.isAccountNonLocked());
        
        // Habilitado (depende de isActive)
        assertTrue(user.isEnabled());
    }
    
    @Test
    @DisplayName("Deve bloquear conta quando desativado")
    void shouldLockAccountWhenDeactivated() {
        user.deactivate();
        
        assertFalse(user.isAccountNonLocked());
        assertFalse(user.isEnabled());
    }
    
    @Test
    @DisplayName("Deve configurar authorities para bibliotecário")
    void shouldSetLibrarianAuthorities() {
        user.setRole(UserRole.LIBRARIAN);
        
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_LIBRARIAN")));
    }
    
    @Test
    @DisplayName("Deve configurar timestamps no onCreate")
    void shouldSetTimestampsOnCreate() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        
        // Simular @PrePersist
        user.onCreate();
        
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertTrue(user.getCreatedAt().isAfter(before));
        assertTrue(user.getCreatedAt().isBefore(after));
        assertEquals(user.getCreatedAt(), user.getUpdatedAt());
    }
    
    @Test
    @DisplayName("Deve atualizar timestamp no onUpdate")
    void shouldUpdateTimestampOnUpdate() throws InterruptedException {
        // Configurar timestamps iniciais
        user.onCreate();
        LocalDateTime originalUpdatedAt = user.getUpdatedAt();
        
        // Aguardar um pouco
        Thread.sleep(10);
        
        // Simular @PreUpdate
        user.onUpdate();
        
        assertNotNull(user.getUpdatedAt());
        assertTrue(user.getUpdatedAt().isAfter(originalUpdatedAt));
    }
    
    @Test
    @DisplayName("Deve implementar equals corretamente")
    void shouldImplementEqualsCorrectly() {
        User user1 = new User(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_PHONE);
        User user2 = new User("Outro Nome", VALID_EMAIL, "outraSenha", "11888888888");
        User user3 = new User(VALID_NAME, "outro@email.com", VALID_PASSWORD, VALID_PHONE);
        
        // Mesmo email e ID = iguais
        user1.setId(1L);
        user2.setId(1L); // Mesmo ID
        assertEquals(user1, user2);
        
        // Email diferente = diferentes
        user3.setId(1L);
        assertNotEquals(user1, user3);
        
        // Null = diferentes
        assertNotEquals(user1, null);
        
        // Classe diferente = diferentes
        assertNotEquals(user1, "string");
    }
    
    @Test
    @DisplayName("Deve implementar hashCode corretamente")
    void shouldImplementHashCodeCorrectly() {
        User user1 = new User(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, VALID_PHONE);
        User user2 = new User("Outro Nome", VALID_EMAIL, "outraSenha", "11888888888");
        
        user1.setId(1L);
        user2.setId(1L); // Mesmo ID
        
        // Mesmo email e ID = mesmo hashCode
        assertEquals(user1.hashCode(), user2.hashCode());
    }
    
    @Test
    @DisplayName("Deve implementar toString corretamente")
    void shouldImplementToStringCorrectly() {
        user.setId(1L);
        
        String toString = user.toString();
        
        // Debug: vamos ver o que está sendo retornado
        System.out.println("ToString result: " + toString);
        
        assertTrue(toString.contains("User{"));
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='" + VALID_NAME + "'"));
        assertTrue(toString.contains("email='" + VALID_EMAIL + "'"));
        
        // Vamos testar se contém STUDENT ou Estudante
        boolean containsStudent = toString.contains("role=STUDENT");
        boolean containsEstudante = toString.contains("role=Estudante"); 
        System.out.println("Contains STUDENT: " + containsStudent);
        System.out.println("Contains Estudante: " + containsEstudante);
        
        assertTrue(containsStudent || containsEstudante, "ToString deve conter role=STUDENT ou role=Estudante");
        assertTrue(toString.contains("isActive=true"));
    }
}