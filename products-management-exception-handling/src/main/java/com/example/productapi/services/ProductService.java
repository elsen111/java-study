package com.example.productapi.services;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.DiscountedPriceResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.entities.CategoryEntity;
import com.example.productapi.entities.ProductEntity;
import com.example.productapi.exception.BadRequestException;
import com.example.productapi.exception.ProductNotFoundException;
import com.example.productapi.exception.ResourceNotFoundException;
import com.example.productapi.mapper.ProductMapper;
import com.example.productapi.repositories.CategoryRepository;
import com.example.productapi.repositories.ProductRepository;
import com.example.productapi.services.utils.ValidationUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

        ProductEntity product = productMapper.toEntity(request);
        product.setCategories(getCategoriesByIds(request.getCategoryIds()));

        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public ProductResponse getProductById(Long id) {
        ProductEntity product = getProductEntityById(id);
        return productMapper.toResponse(product);
    }

    public List<ProductResponse> getAllProducts(BigDecimal minPrice, BigDecimal maxPrice, String name) {
        validatePriceRange(minPrice, maxPrice);

        List<ProductEntity> products;

        if (name != null && !name.trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (minPrice != null && maxPrice != null) {
            products = productRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
            products = productRepository.findAll();
        }

        return productMapper.toResponseList(products);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        validateUpdateRequest(request);

        ProductEntity product = getProductEntityById(id);
        productMapper.updateEntityFromRequest(request, product);

        if (request.getCategoryIds() != null) {
            product.setCategories(getCategoriesByIds(request.getCategoryIds()));
        }

        ProductEntity updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {
        ProductEntity product = getProductEntityById(id);
        productRepository.delete(product);
    }

    public List<ProductResponse> getActiveProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new BadRequestException("Category is required");
        }

        List<ProductEntity> products = productRepository.findDistinctByCategories_NameAndActiveTrue(category.trim());
        return productMapper.toResponseList(products);
    }

    public ProductResponse assignCategoryToProduct(Long productId, Long categoryId) {
        ProductEntity product = getProductEntityById(productId);
        CategoryEntity category = getCategoryEntityById(categoryId);

        product.getCategories().add(category);

        ProductEntity updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    public ProductResponse removeCategoryFromProduct(Long productId, Long categoryId) {
        ProductEntity product = getProductEntityById(productId);
        CategoryEntity category = getCategoryEntityById(categoryId);

        if (!product.getCategories().contains(category)) {
            throw new BadRequestException("This product is not assigned to category id " + categoryId);
        }

        product.getCategories().remove(category);

        ProductEntity updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    public DiscountedPriceResponse getDiscountedPrice(Long id, BigDecimal percent) {
        validateDiscountPercent(percent);

        ProductEntity product = getProductEntityById(id);
        BigDecimal originalPrice = product.getPrice();
        BigDecimal discountAmount = originalPrice
                .multiply(percent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal discountedPrice = originalPrice.subtract(discountAmount);

        return new DiscountedPriceResponse(
                product.getId(),
                product.getName(),
                originalPrice,
                percent,
                discountedPrice
        );
    }

    private ProductEntity getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private CategoryEntity getCategoryEntityById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
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
        Map<String, String> errors = ValidationUtil.newErrorMap();

        if (request == null) {
            errors.put("request", "Request body is required");
            ValidationUtil.throwIfNotEmpty(errors);
        }

        ValidationUtil.requireText(errors, "name", request.getName(), "Product name");
        ValidationUtil.requireValue(errors, "price", request.getPrice(), "Price");
        ValidationUtil.requireValue(errors, "quantity", request.getQuantity(), "Quantity");
        ValidationUtil.requireNotEmpty(errors, "categoryIds", request.getCategoryIds(), "category id");
        ValidationUtil.rejectNegativeWhenPresent(errors, "price", request.getPrice(), "Price");
        ValidationUtil.rejectNegativeWhenPresent(errors, "quantity", request.getQuantity(), "Quantity");

        ValidationUtil.throwIfNotEmpty(errors);
    }

    private void validateUpdateRequest(UpdateProductRequest request) {
        Map<String, String> errors = ValidationUtil.newErrorMap();

        if (request == null) {
            errors.put("request", "Request body is required");
            ValidationUtil.throwIfNotEmpty(errors);
        }

        ValidationUtil.rejectBlankWhenPresent(errors, "name", request.getName(), "Product name");
        ValidationUtil.rejectNegativeWhenPresent(errors, "price", request.getPrice(), "Price");
        ValidationUtil.rejectNegativeWhenPresent(errors, "quantity", request.getQuantity(), "Quantity");

        if (request.getCategoryIds() != null && request.getCategoryIds().isEmpty()) {
            errors.put("categoryIds", "Category ids cannot be empty");
        }

        ValidationUtil.throwIfNotEmpty(errors);
    }

    private void validatePriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new BadRequestException("minPrice cannot be greater than maxPrice");
        }
    }

    private void validateDiscountPercent(BigDecimal percent) {
        Map<String, String> errors = ValidationUtil.newErrorMap();

        ValidationUtil.requireValue(errors, "percent", percent, "Discount percent");

        if (percent != null && percent.compareTo(BigDecimal.ZERO) < 0) {
            errors.put("percent", "Discount percent cannot be negative");
        }
        if (percent != null && percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            errors.put("percent", "Discount percent cannot be greater than 100");
        }

        ValidationUtil.throwIfNotEmpty(errors);
    }
}
