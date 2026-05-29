package com.example.productapi.dto.response;

public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private ProductResponse product;

    public CategoryResponse() {}

    public CategoryResponse(Long id, String name, String description, ProductResponse product) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ProductResponse getProduct() {
        return product;
    }
}