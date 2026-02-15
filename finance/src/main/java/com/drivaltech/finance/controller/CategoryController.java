package com.drivaltech.finance.controller;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.create(category);
    }

    @GetMapping
    public List<Category> findAll() {
        return service.findAll();
    }
}
