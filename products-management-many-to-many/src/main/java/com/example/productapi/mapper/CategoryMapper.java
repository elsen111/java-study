package com.example.productapi.mapper;

import com.example.productapi.dto.request.CreateCategoryRequest;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.entities.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CreateCategoryRequest request) {
        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
