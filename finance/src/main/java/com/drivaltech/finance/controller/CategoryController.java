package com.drivaltech.finance.controller;

import com.drivaltech.finance.dto.CategoryRequestDTO;
import com.drivaltech.finance.dto.CategoryResponseDTO;
import com.drivaltech.finance.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public CategoryResponseDTO create(@Valid @RequestBody CategoryRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<CategoryResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDTO dto) {

        return service.update(id, dto);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
