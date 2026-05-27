package com.example.productapi.mapper;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.CategoryResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductMapper {

    public ProductResponse toResponse(ProductEntity entity) {
        ProductResponse response = new ProductResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setPrice(entity.getPrice());
        response.setQuantity(entity.getQuantity());
        response.setActive(entity.getActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setTotalPrice(calculateTotalPrice(entity));

        List<CategoryResponse> categories = entity.getCategories()
                .stream()
                .map(this::toCategoryResponse)
                .toList();
        response.setCategories(categories);

        return response;
    }

    public List<ProductResponse> toResponseList(List<ProductEntity> entities) {
        return entities.stream().map(this::toResponse).toList();
    }

    public ProductEntity toEntity(CreateProductRequest request) {
        ProductEntity entity = new ProductEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setQuantity(request.getQuantity());
        entity.setActive(request.getActive());
        return entity;
    }

    public void updateEntityFromRequest(UpdateProductRequest request, ProductEntity entity) {
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            entity.setPrice(request.getPrice());
        }
        if (request.getQuantity() != null) {
            entity.setQuantity(request.getQuantity());
        }
        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }

    private CategoryResponse toCategoryResponse(CategoryEntity category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private BigDecimal calculateTotalPrice(ProductEntity entity) {
        if (entity == null || entity.getPrice() == null || entity.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return entity.getPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
    }
}
