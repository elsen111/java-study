package com.example.productapi.services;

import com.example.productapi.dto.request.CreateCategoryRequest;
import com.example.productapi.dto.request.UpdateCategoryRequest;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.mapper.CategoryMapper;
import com.example.productapi.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        validateCreateRequest(request);

        CategoryEntity category = categoryMapper.toEntity(request);
        CategoryEntity savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        CategoryEntity category = getEntityById(id);
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        CategoryEntity category = getEntityById(id);

        if (request.getName() != null) {
            if (request.getName().trim().isEmpty()) {
                throw new BadRequestException("Category name cannot be empty");
            }

            category.setName(request.getName());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        CategoryEntity updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    public void deleteCategory(Long id) {
        CategoryEntity category = getEntityById(id);
        categoryRepository.delete(category);
    }

    private CategoryEntity getEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Category not found with id: " + id));
    }

    private void validateCreateRequest(CreateCategoryRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }
    }
}