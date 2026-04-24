package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.dto.CategoryRequestDTO;
import com.drivaltech.finance.dto.CategoryResponseDTO;
import com.drivaltech.finance.exception.ForbiddenException;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import com.drivaltech.finance.repository.CategoryRepository;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(
            CategoryRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    // CREATE
    public CategoryResponseDTO create(CategoryRequestDTO dto) {

        User user = getAuthenticatedUser();

        Category category = new Category(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getColor(),
                true,
                dto.getType()
        );

        category.setUser(user); // vínculo com usuário

        logger.info("Creating category | userId={} | name={}",
                user.getId(), dto.getName());

        Category saved = repository.save(category);

        return toResponse(saved);
    }

    // FIND BY ID
    public CategoryResponseDTO findById(UUID id) {

        User user = getAuthenticatedUser();

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        validateOwnership(category, user);

        logger.info("Fetching category | id={} | userId={}", id, user.getId());

        return toResponse(category);
    }

    // FIND ALL (SOMENTE DO USUÁRIO)
    public List<CategoryResponseDTO> findAll() {

        User user = getAuthenticatedUser();

        logger.info("Fetching categories | userId={}", user.getId());

        return repository.findByUserAndActiveTrue(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // UPDATE
    public CategoryResponseDTO update(UUID id, CategoryRequestDTO dto) {

        User user = getAuthenticatedUser();

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        validateOwnership(category, user);

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setColor(dto.getColor());

        Category updated = repository.save(category);

        logger.info("Updating category | id={} | userId={}", id, user.getId());

        return toResponse(updated);
    }

    // DELETE (soft delete)
    public void delete(UUID id) {

        User user = getAuthenticatedUser();

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id: " + id)
                );

        validateOwnership(category, user);

        category.deactivate();

        repository.save(category);

        logger.warn("Deactivating category | id={} | userId={}", id, user.getId());
    }

    // VALIDAÇÃO DE DONO
    private void validateOwnership(Category category, User user) {
        if (!category.getUser().getId().equals(user.getId())) {
            logger.warn("Unauthorized category access | userId={} | categoryId={}",
                    user.getId(), category.getId());
            throw new ForbiddenException("You do not have permission to access this category");
        }
    }

    // MAPPER
    private CategoryResponseDTO toResponse(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor(),
                category.isActive(),
                category.getType()
        );
    }

    // AUTH
    private String getLoggedUsername() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return authentication.getName();
    }

    private User getAuthenticatedUser() {
        String username = getLoggedUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}