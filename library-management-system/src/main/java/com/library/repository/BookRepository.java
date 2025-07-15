package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Verificar se ISBN já existe
     */
    boolean existsByIsbn(String isbn);
    
    /**
     * Buscar livro por ISBN
     */
    Optional<Book> findByIsbn(String isbn);
    
    /**
     * Buscar livros por disponibilidade
     */
    List<Book> findByAvailable(Boolean available);
    
    /**
     * Buscar livros por categoria
     */
    List<Book> findByCategory(String category);
    
    /**
     * Buscar livros por título (contém)
     */
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Buscar livros por autor (contém)
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    /**
     * Busca global por título, autor ou ISBN
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "b.isbn LIKE CONCAT('%', :search, '%')")
    List<Book> searchBooks(@Param("search") String search);
    
    /**
     * Contar livros disponíveis
     */
    @Query("SELECT COUNT(b) FROM Book b WHERE b.available = true")
    Long countAvailableBooks();
    
    /**
     * Contar total de livros
     */
    @Query("SELECT COUNT(b) FROM Book b")
    Long countTotalBooks();
    
    /**
     * Estatísticas por categoria
     */
    @Query("SELECT b.category, COUNT(b) FROM Book b GROUP BY b.category ORDER BY COUNT(b) DESC")
    List<Object[]> getBookCountByCategory();
    
    /**
     * Livros mais recentes
     */
    @Query("SELECT b FROM Book b ORDER BY b.createdAt DESC")
    List<Book> findRecentBooks();
}
