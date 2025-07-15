package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class BookController {
    
    private final BookService bookService;
    
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    /**
     * GET /api/books - Listar todos os livros
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }
    
    /**
     * GET /api/books/{id} - Buscar livro por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/books/isbn/{isbn} - Buscar livro por ISBN
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /api/books - Criar novo livro
     */
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookRequest request) {
        try {
            Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getIsbn(),
                request.getCategory()
            );
            book.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
            
            Book savedBook = bookService.createBook(book);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * PUT /api/books/{id} - Atualizar livro
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        try {
            Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getIsbn(),
                request.getCategory()
            );
            book.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
            
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * DELETE /api/books/{id} - Deletar livro
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * GET /api/books/available - Listar livros disponíveis
     */
    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        List<Book> books = bookService.findAvailableBooks();
        return ResponseEntity.ok(books);
    }
    
    /**
     * GET /api/books/category/{category} - Buscar por categoria
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable String category) {
        List<Book> books = bookService.findBooksByCategory(category);
        return ResponseEntity.ok(books);
    }
    
    /**
     * GET /api/books/search - Busca global
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String q) {
        List<Book> books = bookService.searchBooks(q);
        return ResponseEntity.ok(books);
    }
    
    /**
     * GET /api/books/stats - Estatísticas dos livros
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBookStatistics() {
        Map<String, Object> stats = bookService.getBookStatistics();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * GET /api/books/recent - Livros mais recentes
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Book>> getRecentBooks(@RequestParam(defaultValue = "5") int limit) {
        List<Book> books = bookService.getRecentBooks(limit);
        return ResponseEntity.ok(books);
    }
    
    /**
     * PUT /api/books/{id}/loan - Marcar como emprestado
     */
    @PutMapping("/{id}/loan")
    public ResponseEntity<?> loanBook(@PathVariable Long id) {
        try {
            bookService.markAsLoaned(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * PUT /api/books/{id}/return - Marcar como disponível
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        try {
            bookService.markAsAvailable(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // DTOs
    public static class BookRequest {
        private String title;
        private String author;
        private String isbn;
        private String category;
        private Boolean available;
        
        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
    }
    
    public static class ErrorResponse {
        private String error;
        
        public ErrorResponse(String error) {
            this.error = error;
        }
        
        public String getError() { return error; }
    }
}