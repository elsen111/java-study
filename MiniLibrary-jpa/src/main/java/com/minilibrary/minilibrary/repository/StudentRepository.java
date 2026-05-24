package com.minilibrary.minilibrary.repository;

import com.minilibrary.minilibrary.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
}
