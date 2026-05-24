package com.minilibrary.minilibrary.repository;

import com.minilibrary.minilibrary.entity.BorrowRecord;
import com.minilibrary.minilibrary.entity.BorrowRecord.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsByBookIdAndStudentIdAndStatus(Long bookId, Long studentId, Status status);

    List<BorrowRecord> findByStatus(Status status);

    List<BorrowRecord> findByStudentId(Long studentId);

    @Query("""
            SELECT b.id, b.title, COUNT(br.id) AS borrowCount
            FROM BorrowRecord br
            JOIN br.book b
            GROUP BY b.id, b.title
            ORDER BY borrowCount DESC
            """)
    List<Object[]> findMostBorrowedBooks();
}
