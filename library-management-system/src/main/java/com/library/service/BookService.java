package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Listar todos os livros
     */
    @Transactional(readOnly = true)
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Buscar livro por ID
     */
    @Transactional(readOnly = true)
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }
    
    /**
     * Buscar livro por ISBN
     */
    @Transactional(readOnly = true)
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
    
    /**
     * Criar novo livro
     */
    public Book createBook(Book book) {
        validateBook(book);
        
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN já existe: " + book.getIsbn());
        }
        
        return bookRepository.save(book);
    }
    
    /**
     * Atualizar livro
     */
    public Book updateBook(Long id, Book bookData) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        
        // Verificar se ISBN já existe em outro livro
        if (!existingBook.getIsbn().equals(bookData.getIsbn()) && 
            bookRepository.existsByIsbn(bookData.getIsbn())) {
            throw new IllegalArgumentException("ISBN já existe: " + bookData.getIsbn());
        }
        
        validateBook(bookData);
        
        existingBook.setTitle(bookData.getTitle());
        existingBook.setAuthor(bookData.getAuthor());
        existingBook.setIsbn(bookData.getIsbn());
        existingBook.setCategory(bookData.getCategory());
        existingBook.setAvailable(bookData.getAvailable());
        
        return bookRepository.save(existingBook);
    }
    
    /**
     * Deletar livro
     */
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        
        bookRepository.delete(book);
    }
    
    /**
     * Buscar livros disponíveis
     */
    @Transactional(readOnly = true)
    public List<Book> findAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }
    
    /**
     * Buscar livros por categoria
     */
    @Transactional(readOnly = true)
    public List<Book> findBooksByCategory(String category) {
        return bookRepository.findByCategory(category);
    }
    
    /**
     * Busca global
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String search) {
        if (search == null || search.trim().isEmpty()) {
            return findAllBooks();
        }
        return bookRepository.searchBooks(search.trim());
    }
    
    /**
     * Estatísticas dos livros
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getBookStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        Long totalBooks = bookRepository.countTotalBooks();
        Long availableBooks = bookRepository.countAvailableBooks();
        Long loanedBooks = totalBooks - availableBooks;
        
        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("loanedBooks", loanedBooks);
        
        // Livros por categoria
        List<Object[]> categoryStats = bookRepository.getBookCountByCategory();
        stats.put("booksByCategory", categoryStats);
        
        return stats;
    }
    
    /**
     * Livros mais recentes
     */
    @Transactional(readOnly = true)
    public List<Book> getRecentBooks(int limit) {
        List<Book> recentBooks = bookRepository.findRecentBooks();
        return recentBooks.stream().limit(limit).toList();
    }
    
    /**
     * Marcar livro como emprestado
     */
    public void markAsLoaned(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        
        if (!book.getAvailable()) {
            throw new IllegalStateException("Livro já está emprestado");
        }
        
        book.setAvailable(false);
        bookRepository.save(book);
    }
    
    /**
     * Marcar livro como disponível
     */
    public void markAsAvailable(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        
        book.setAvailable(true);
        bookRepository.save(book);
    }
    
    /**
     * Validar dados do livro
     */
    private void validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN é obrigatório");
        }
        
        // Validação básica de ISBN (pode ser melhorada)
        String isbn = book.getIsbn().replaceAll("[^0-9]", "");
        if (isbn.length() != 10 && isbn.length() != 13) {
            throw new IllegalArgumentException("ISBN deve ter 10 ou 13 dígitos");
        }
    }
}