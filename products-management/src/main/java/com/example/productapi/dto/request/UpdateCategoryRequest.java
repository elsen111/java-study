package com.example.productapi.dto.request;

public class UpdateCategoryRequest {

    private String name;
    private String description;

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
}