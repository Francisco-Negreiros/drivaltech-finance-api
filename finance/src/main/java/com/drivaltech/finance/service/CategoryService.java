package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.dto.CategoryRequestDTO;
import com.drivaltech.finance.dto.CategoryResponseDTO;
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

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());
        category.setActive(true);

        Category saved = repository.save(category);

        return new CategoryResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getColor(),
                saved.getActive()
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
                category.getActive()
        );
    }


    public List<CategoryResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(category -> new CategoryResponseDTO(
                        category.getId(),
                        category.getName(),
                        category.getDescription(),
                        category.getColor(),
                        category.getActive()
                ))
                .toList();
    }
}
