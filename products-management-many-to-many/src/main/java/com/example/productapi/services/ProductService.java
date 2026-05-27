package com.example.productapi.services;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.DiscountedPriceResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ProductNotFoundException;
import com.example.productapi.mapper.ProductMapper;
import com.example.productapi.repositories.CategoryRepository;
import com.example.productapi.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        validateCreateRequest(request);

        ProductEntity entity = productMapper.toEntity(request);
        entity.setCategories(getCategoriesByIds(request.getCategoryIds()));

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

        if (request.getCategoryIds() != null) {
            entity.setCategories(getCategoriesByIds(request.getCategoryIds()));
        }

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
        List<ProductEntity> products = productRepository.findDistinctByCategories_NameAndActiveTrue(category.trim());
        return productMapper.toResponseList(products);
    }



    public ProductResponse assignCategoryToProduct(Long productId, Long categoryId) {
        ProductEntity product = getEntityById(productId);
        CategoryEntity category = getCategoryEntityById(categoryId);

        product.getCategories().add(category);

        ProductEntity updatedEntity = productRepository.save(product);
        return productMapper.toResponse(updatedEntity);
    }

    public ProductResponse removeCategoryFromProduct(Long productId, Long categoryId) {
        ProductEntity product = getEntityById(productId);
        CategoryEntity category = getCategoryEntityById(categoryId);

        if (!product.getCategories().contains(category)) {
            throw new BadRequestException("This product does not contain category id " + categoryId);
        }

        product.getCategories().remove(category);

        ProductEntity updatedEntity = productRepository.save(product);
        return productMapper.toResponse(updatedEntity);
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

    private CategoryEntity getCategoryEntityById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Category not found with id " + categoryId));
    }

    private Set<CategoryEntity> getCategoriesByIds(List<Long> categoryIds) {
        List<Long> uniqueIds = categoryIds.stream().distinct().toList();
        List<CategoryEntity> categories = categoryRepository.findAllById(uniqueIds);

        if (categories.size() != uniqueIds.size()) {
            throw new BadRequestException("One or more category ids are invalid");
        }

        return new HashSet<>(categories);
    }

    private void validateCreateRequest(CreateProductRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (request.getCategoryIds() == null || request.getCategoryIds().isEmpty()) {
            throw new BadRequestException("At least one category id is required");
        }
    }

    private void validateUpdateRequest(UpdateProductRequest request) {
        if (request.getName() != null && request.getName().trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (request.getCategoryIds() != null && request.getCategoryIds().isEmpty()) {
            throw new BadRequestException("Category ids cannot be empty");
        }
    }
}
