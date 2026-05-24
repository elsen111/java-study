package com.minilibrary.minilibrary.service;

import com.minilibrary.minilibrary.dto.request.BorrowRequest;
import com.minilibrary.minilibrary.dto.response.BorrowResponse;
import com.minilibrary.minilibrary.entity.Book;
import com.minilibrary.minilibrary.entity.BorrowRecord;
import com.minilibrary.minilibrary.entity.BorrowRecord.Status;
import com.minilibrary.minilibrary.entity.Student;
import com.minilibrary.minilibrary.exception.BadRequestException;
import com.minilibrary.minilibrary.exception.NotFoundException;
import com.minilibrary.minilibrary.repository.BookRepository;
import com.minilibrary.minilibrary.repository.BorrowRecordRepository;
import com.minilibrary.minilibrary.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository,
                         BookRepository bookRepository,
                         StudentRepository studentRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public void borrowBook(BorrowRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Book not available");
        }

        if (borrowRecordRepository.existsByBookIdAndStudentIdAndStatus(book.getId(), student.getId(), Status.BORROWED)) {
            throw new BadRequestException("Student already borrowed this book and has not returned it");
        }

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setStudent(student);
        record.setStatus(Status.BORROWED);
        borrowRecordRepository.save(record);

        bookRepository.decreaseAvailableQuantity(book.getId());
    }

    @Transactional
    public void returnBook(Long borrowId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new NotFoundException("Borrow record not found"));

        if (record.getStatus() == Status.RETURNED) {
            throw new BadRequestException("This book is already returned");
        }

        record.setStatus(Status.RETURNED);
        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        bookRepository.increaseAvailableQuantity(record.getBook().getId());
    }

    public List<BorrowResponse> getAll() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(BorrowResponse::from)
                .toList();
    }

    public List<BorrowResponse> getActiveBorrows() {
        return borrowRecordRepository.findByStatus(Status.BORROWED)
                .stream()
                .map(BorrowResponse::from)
                .toList();
    }

    public List<BorrowResponse> getStudentBorrows(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new NotFoundException("Student not found");
        }

        return borrowRecordRepository.findByStudentId(studentId)
                .stream()
                .map(BorrowResponse::from)
                .toList();
    }

    public List<Map<String, Object>> mostBorrowedBooks() {
        return borrowRecordRepository.findMostBorrowedBooks()
                .stream()
                .map(row -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("id", row[0]);
                    entry.put("title", row[1]);
                    entry.put("borrow_count", row[2]);
                    return entry;
                })
                .toList();
    }
}
