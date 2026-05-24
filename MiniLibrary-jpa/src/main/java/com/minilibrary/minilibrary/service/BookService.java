package com.minilibrary.minilibrary.service;

import com.minilibrary.minilibrary.dto.request.BookCreateRequest;
import com.minilibrary.minilibrary.dto.response.BookResponse;
import com.minilibrary.minilibrary.entity.Book;
import com.minilibrary.minilibrary.exception.BadRequestException;
import com.minilibrary.minilibrary.exception.NotFoundException;
import com.minilibrary.minilibrary.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void create(BookCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new BadRequestException("Title cannot be empty");
        }

        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BadRequestException("ISBN already exists");
        }

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setQuantity(request.getQuantity());
        book.setAvailableQuantity(request.getQuantity());

        bookRepository.save(book);
    }

    public List<BookResponse> getAll() {
        return bookRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));
        return BookResponse.from(book);
    }

    public List<BookResponse> search(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookResponse::from)
                .toList();
    }

    public List<BookResponse> getOutOfStockBooks() {
        return bookRepository.findByAvailableQuantity(0)
                .stream()
                .map(BookResponse::from)
                .toList();
    }
}
