package com.example.productapi.services;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.DiscountedPriceResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.ProductEntity;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ProductNotFoundException;
import com.example.productapi.mapper.ProductMapper;
import com.example.productapi.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        validateCreateRequest(request);
        ProductEntity entity = productMapper.toEntity(request);
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toResponse(savedEntity);
    }

    public ProductResponse getProductById(Long id) {
        ProductEntity entity = getEntityById(id);
        return productMapper.toResponse(entity);
    }

    public List<ProductResponse> getAllProducts(BigDecimal minPrice, BigDecimal maxPrice, String name) {
        List<ProductEntity> products;

        if (name != null && !name.trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (minPrice != null && maxPrice != null) {
            if (minPrice.compareTo(maxPrice) > 0) {
                throw new BadRequestException("minPrice cannot be greater than maxPrice");
            }
            products = productRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            products = productRepository.findAll();
        }

        return productMapper.toResponseList(products);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        validateUpdateRequest(request);
        ProductEntity entity = getEntityById(id);
        productMapper.updateEntityFromRequest(request, entity);
        ProductEntity updatedEntity = productRepository.save(entity);
        return productMapper.toResponse(updatedEntity);
    }

    public void deleteProduct(Long id) {
        ProductEntity entity = getEntityById(id);
        productRepository.delete(entity);
    }

    public List<ProductResponse> getActiveProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new BadRequestException("Category cannot be empty");
        }
        List<ProductEntity> products = productRepository.findByCategory_NameAndActiveTrue(category.trim());
        return productMapper.toResponseList(products);
    }

    public DiscountedPriceResponse getDiscountedPrice(Long id, BigDecimal percent) {
        if (percent == null) {
            throw new BadRequestException("Discount percent is required");
        }
        if (percent.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Discount percent cannot be negative");
        }
        if (percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BadRequestException("Discount percent cannot be greater than 100");
        }

        ProductEntity entity = getEntityById(id);
        BigDecimal originalPrice = entity.getPrice();
        BigDecimal discountAmount = originalPrice
                .multiply(percent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discountedPrice = originalPrice.subtract(discountAmount);

        return new DiscountedPriceResponse(
                entity.getId(),
                entity.getName(),
                originalPrice,
                percent,
                discountedPrice
        );
    }

    private ProductEntity getEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private void validateCreateRequest(CreateProductRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (request.getCategoryId() == null) {
            throw new BadRequestException("Category id is required");
        }
    }

    private void validateUpdateRequest(UpdateProductRequest request) {
        if (request.getName() != null && request.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (request.getCategoryId() == null) {
            throw new BadRequestException("Category id is required");
        }
    }
}
