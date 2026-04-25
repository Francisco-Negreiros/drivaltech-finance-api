package com.drivaltech.finance.dto;

import jakarta.validation.constraints.NotBlank;
import com.drivaltech.finance.domain.CategoryType;

public class CategoryRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String color;

    private CategoryType type;

    public CategoryRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public CategoryType getType() {return type;}

    // Getters and Setters
}

