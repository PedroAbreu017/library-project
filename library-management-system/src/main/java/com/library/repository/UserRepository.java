package com.library.repository;

import com.library.entity.User;
import com.library.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Busca usuário por email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Verifica se existe usuário com o email
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários ativos
     */
    List<User> findByIsActiveTrue();
    
    /**
     * Busca usuários por papel
     */
    List<User> findByRole(UserRole role);
    
    /**
     * Busca usuários ativos por papel
     */
    List<User> findByRoleAndIsActiveTrue(UserRole role);
    
    /**
     * Busca usuários por nome (contém)
     */
    List<User> findByNameContainingIgnoreCase(String name);
    
    /**
     * Busca usuários ativos por nome
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> findActiveUsersByNameContaining(@Param("name") String name);
    
    /**
     * Conta usuários por papel
     */
    long countByRole(UserRole role);
    
    /**
     * Conta usuários ativos
     */
    long countByIsActiveTrue();
    
    /**
     * Busca usuários com empréstimos pendentes
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.loans l WHERE l.returned = false")
    List<User> findUsersWithPendingLoans();
    
    /**
     * Busca usuários com reservas ativas
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.reservations r WHERE r.active = true")
    List<User> findUsersWithActiveReservations();

    /**
    * Buscar usuários ativos ordenados por data de criação
    */
   List<User> findByIsActiveTrueOrderByCreatedAtDesc();
}
