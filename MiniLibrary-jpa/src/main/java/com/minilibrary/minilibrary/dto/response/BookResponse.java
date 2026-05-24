package com.minilibrary.minilibrary.dto.response;

import com.minilibrary.minilibrary.entity.Book;

import java.time.LocalDateTime;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
    private Integer availableQuantity;
    private LocalDateTime createdAt;

    public BookResponse(Long id, String title, String author, String isbn,
                        Integer quantity, Integer availableQuantity, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
        this.createdAt = createdAt;
    }

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getQuantity(),
                book.getAvailableQuantity(),
                book.getCreatedAt()
        );
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public Integer getQuantity() { return quantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
