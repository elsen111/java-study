package com.example.productapi.dto.request;

public class CreateCategoryRequest {

    private String name;
    private String description;
    private CreateProductRequest product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateProductRequest getProduct() {
        return product;
    }

    public void setProduct(CreateProductRequest product) {
        this.product = product;
    }
}