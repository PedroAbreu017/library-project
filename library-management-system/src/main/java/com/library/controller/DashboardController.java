package com.library.controller;

import com.library.service.BookService;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    private final UserService userService;
    private final BookService bookService;
    
    @Autowired
    public DashboardController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }
    
    /**
     * GET /api/dashboard/stats - Estatísticas gerais do dashboard
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Estatísticas de usuários
        Map<String, Object> userStats = userService.getUserStatistics();
        stats.put("users", userStats);
        
        // Estatísticas de livros
        Map<String, Object> bookStats = bookService.getBookStatistics();
        stats.put("books", bookStats);
        
        // Estatísticas de empréstimos (mock por enquanto)
        Map<String, Object> loanStats = new HashMap<>();
        loanStats.put("activeLoans", 0);
        loanStats.put("overdueLoans", 0);
        loanStats.put("totalLoans", 0);
        stats.put("loans", loanStats);
        
        // Atividade mensal (mock)
        stats.put("monthlyActivity", 98);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * GET /api/dashboard/recent-activity - Atividade recente
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<Map<String, Object>> getRecentActivity() {
        Map<String, Object> activity = new HashMap<>();
        
        // Usuários recentes
        activity.put("recentUsers", userService.getRecentUsers(5));
        
        // Livros recentes
        activity.put("recentBooks", bookService.getRecentBooks(5));
        
        return ResponseEntity.ok(activity);
    }
}