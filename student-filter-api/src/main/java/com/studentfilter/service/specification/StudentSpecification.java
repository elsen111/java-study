package com.studentfilter.service.specification;

import com.studentfilter.model.Student;
import com.studentfilter.model.StudentFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class StudentSpecification implements Specification<Student> {

    private StudentFilterDto studentFilterDto;

    @Override
    public @Nullable Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (studentFilterDto == null) {
            return criteriaBuilder.conjunction(); // Boş filtr gəldikdə bütün tələbələri qaytarır (WHERE 1=1)
        }

        List<Predicate> predicates = new ArrayList<>();

        if (studentFilterDto.getFullName() != null && !studentFilterDto.getFullName().trim().isEmpty()) {
            String pattern = "%" + studentFilterDto.getFullName().trim().toLowerCase() + "%";
            Predicate fullNamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("fullName")),
                    pattern
            );
            predicates.add(fullNamePredicate);
        }

        if (studentFilterDto.getFromAge() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age").as(String.class), studentFilterDto.getFromAge().toString()));
        }

        if (studentFilterDto.getToAge() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age").as(String.class), studentFilterDto.getToAge().toString()));
        }

        if (studentFilterDto.getFromGpa() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("gpa"), studentFilterDto.getFromGpa()));
        }

        if (studentFilterDto.getToGpa() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("gpa"), studentFilterDto.getToGpa()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
