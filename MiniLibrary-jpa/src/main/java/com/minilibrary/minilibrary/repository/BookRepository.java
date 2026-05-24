package com.minilibrary.minilibrary.repository;

import com.minilibrary.minilibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAvailableQuantity(int availableQuantity);

    @Modifying
    @Query("UPDATE Book b SET b.availableQuantity = b.availableQuantity - 1 WHERE b.id = :bookId")
    void decreaseAvailableQuantity(@Param("bookId") Long bookId);

    @Modifying
    @Query("UPDATE Book b SET b.availableQuantity = b.availableQuantity + 1 WHERE b.id = :bookId")
    void increaseAvailableQuantity(@Param("bookId") Long bookId);
}
