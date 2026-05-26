package com.example.productapi.services;

import com.example.productapi.dto.request.CreateCategoryRequest;
import com.example.productapi.dto.request.UpdateCategoryRequest;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.mapper.CategoryMapper;
import com.example.productapi.repositories.CategoryRepository;
import com.example.productapi.services.utils.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        CategoryEntity category = getCategoryEntityById(id);
        return categoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        validateUpdateRequest(request);

        CategoryEntity category = getCategoryEntityById(id);

        if (request.getName() != null) {
            category.setName(request.getName().trim());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }

        CategoryEntity updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity category = getCategoryEntityById(id);

        for (ProductEntity product : category.getProducts()) {
            product.getCategories().remove(category);
        }

        categoryRepository.delete(category);
    }

    private CategoryEntity getCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private void validateCreateRequest(CreateCategoryRequest request) {
        Map<String, String> errors = ValidationUtil.newErrorMap();

        if (request == null) {
            errors.put("request", "Request body is required");
            ValidationUtil.throwIfNotEmpty(errors);
        }

        ValidationUtil.requireText(errors, "name", request.getName(), "Category name");
        ValidationUtil.throwIfNotEmpty(errors);
    }

    private void validateUpdateRequest(UpdateCategoryRequest request) {
        Map<String, String> errors = ValidationUtil.newErrorMap();

        if (request == null) {
            errors.put("request", "Request body is required");
            ValidationUtil.throwIfNotEmpty(errors);
        }

        ValidationUtil.rejectBlankWhenPresent(errors, "name", request.getName(), "Category name");
        ValidationUtil.throwIfNotEmpty(errors);
    }
}
