package com.example.productapi.mapper;

import com.example.productapi.dto.request.CreateCategoryRequest;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    private final ProductMapper productMapper;

    public CategoryMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public CategoryEntity toEntity(CreateCategoryRequest request) {
        CategoryEntity category = new CategoryEntity();

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        if (request.getProduct() != null) {
            ProductEntity product = productMapper.toEntity(request.getProduct());
            category.setProduct(product);
            product.setCategory(category);
        }

        return category;
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        ProductResponse productResponse = null;

        if (entity.getProduct() != null) {
            productResponse = productMapper.toResponse(entity.getProduct());
        }

        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                productResponse
        );
    }
}