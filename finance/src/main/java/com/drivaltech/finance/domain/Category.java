package com.drivaltech.finance.domain;

import com.drivaltech.finance.user.User;
import jakarta.persistence.*;
import com.drivaltech.finance.domain.CategoryType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.UUID;

@Entity
@Table(name = "tb_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private String color;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category() {
    }

    public Category(UUID id, String name, String description, String color, Boolean active, CategoryType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.active = active;
        this.type = type;
    }

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    public UUID getId() {
        return id;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoryType getType() {return type;}

    public void setType(CategoryType type) {this.type = type;}

    // getters e setters
}

