package com.minilibrary.minilibrary.controller;

import com.minilibrary.minilibrary.dto.request.BorrowRequest;
import com.minilibrary.minilibrary.dto.response.BorrowResponse;
import com.minilibrary.minilibrary.service.BorrowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/api/borrows")
    public String borrowBook(@RequestBody BorrowRequest request) {
        borrowService.borrowBook(request);
        return "Book borrowed successfully";
    }

    @PostMapping("/api/borrows/{borrowId}/return")
    public String returnBook(@PathVariable Long borrowId) {
        borrowService.returnBook(borrowId);
        return "Book returned successfully";
    }

    @GetMapping("/api/borrows")
    public List<BorrowResponse> getAll() {
        return borrowService.getAll();
    }

    @GetMapping("/api/borrows/active")
    public List<BorrowResponse> getActiveBorrows() {
        return borrowService.getActiveBorrows();
    }

    @GetMapping("/api/reports/most-borrowed-books")
    public List<Map<String, Object>> mostBorrowedBooks() {
        return borrowService.mostBorrowedBooks();
    }
}