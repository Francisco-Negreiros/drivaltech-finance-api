package com.drivaltech.finance.dto;

import java.util.UUID;

public class CategoryResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String color;
    private Boolean active;

    public CategoryResponseDTO(UUID id, String name, String description, String color, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.active = active;
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
}

