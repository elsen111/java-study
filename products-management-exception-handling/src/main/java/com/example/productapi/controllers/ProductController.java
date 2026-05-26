package com.example.productapi.controllers;

import com.example.productapi.dto.request.CreateProductRequest;
import com.example.productapi.dto.request.UpdateProductRequest;
import com.example.productapi.dto.response.DiscountedPriceResponse;
import com.example.productapi.dto.response.ProductResponse;
import com.example.productapi.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String name
    ) {
        return ResponseEntity.ok(productService.getAllProducts(minPrice, maxPrice, name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getActiveProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getActiveProductsByCategory(category));
    }



    @PostMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductResponse> assignCategoryToProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(productService.assignCategoryToProduct(productId, categoryId));
    }

    @DeleteMapping("/{productId}/categories/{categoryId}")
    public ResponseEntity<ProductResponse> removeCategoryFromProduct(
            @PathVariable Long productId,
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.ok(productService.removeCategoryFromProduct(productId, categoryId));
    }

    @GetMapping("/{id}/discounted-price")
    public ResponseEntity<DiscountedPriceResponse> getDiscountedPrice(
            @PathVariable Long id,
            @RequestParam BigDecimal percent
    ) {
        return ResponseEntity.ok(productService.getDiscountedPrice(id, percent));
    }
}
