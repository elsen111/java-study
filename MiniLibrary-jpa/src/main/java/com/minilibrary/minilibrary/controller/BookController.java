package com.minilibrary.minilibrary.controller;

import com.minilibrary.minilibrary.dto.request.BookCreateRequest;
import com.minilibrary.minilibrary.dto.response.BookResponse;
import com.minilibrary.minilibrary.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public String create(@RequestBody BookCreateRequest request) {
        bookService.create(request);
        return "Book created successfully";
    }

    @GetMapping
    public List<BookResponse> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @GetMapping("/search")
    public List<BookResponse> search(@RequestParam String title) {
        return bookService.search(title);
    }

    @GetMapping("/out-of-stock")
    public List<BookResponse> getOutOfStockBooks() {
        return bookService.getOutOfStockBooks();
    }
}