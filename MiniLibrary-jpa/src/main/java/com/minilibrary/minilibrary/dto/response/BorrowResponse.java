package com.minilibrary.minilibrary.dto.response;

import com.minilibrary.minilibrary.entity.BorrowRecord;

import java.time.LocalDateTime;

public class BorrowResponse {

    private Long borrowId;
    private String bookTitle;
    private String studentName;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private String status;

    public BorrowResponse(Long borrowId, String bookTitle, String studentName,
                          LocalDateTime borrowDate, LocalDateTime returnDate, String status) {
        this.borrowId = borrowId;
        this.bookTitle = bookTitle;
        this.studentName = studentName;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public static BorrowResponse from(BorrowRecord record) {
        return new BorrowResponse(
                record.getId(),
                record.getBook().getTitle(),
                record.getStudent().getFullName(),
                record.getBorrowDate(),
                record.getReturnDate(),
                record.getStatus().name()
        );
    }

    public Long getBorrowId() { return borrowId; }
    public String getBookTitle() { return bookTitle; }
    public String getStudentName() { return studentName; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public String getStatus() { return status; }
}
