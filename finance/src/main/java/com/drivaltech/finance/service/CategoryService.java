package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.dto.CategoryRequestDTO;
import com.drivaltech.finance.dto.CategoryResponseDTO;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import com.drivaltech.finance.repository.CategoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryResponseDTO create(CategoryRequestDTO dto) {

        Category category = new Category(
        null,
        dto.getName(),
        dto.getDescription(),
        dto.getColor(),
        true
        );

        Category saved = repository.save(category);

        return new CategoryResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getColor(),
                saved.isActive()
        );
    }

    public CategoryResponseDTO findById(UUID id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                );

        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor(),
                category.isActive()
        );
    }

    public List<CategoryResponseDTO> findAll() {
        return repository.findByActiveTrue()
                .stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.getColor(),
                        category.isActive()
                ))
                .toList();
    }

    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                );

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());

        Category updated = repository.save(category);

        return new CategoryResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getColor(),
                updated.isActive()
        );
    }

    public void delete(UUID id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        //new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
                        new ResourceNotFoundException("Category not found")
                );

        category.deactivate();

        repository.save(category);
    }
}