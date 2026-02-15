package com.drivaltech.finance.controller;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Category create(@RequestBody Category category) {
        return repository.save(category);
    }

    @GetMapping
    public List<Category> findAll() {
        return repository.findAll();
    }
}

