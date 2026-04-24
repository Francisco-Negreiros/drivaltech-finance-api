package com.drivaltech.finance.dto;

import java.util.UUID;
import com.drivaltech.finance.domain.CategoryType;

public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String color;
    private Boolean active;
    private CategoryType type;

    public CategoryResponseDTO(UUID id, String name, String description, String color, Boolean active, CategoryType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.active = active;
        this.type = type;
    }

    public UUID getId() {
        return id;
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

    public Boolean getActive() {
        return active;
    }

    public CategoryType getType() {return type;}

    public void setType(CategoryType type) {this.type = type;}
}

