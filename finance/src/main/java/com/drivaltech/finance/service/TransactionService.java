package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.CreateTransactionRequest;
import com.drivaltech.finance.dto.PaginationResponse;
import com.drivaltech.finance.dto.TransactionResponse;
import com.drivaltech.finance.exception.BusinessException;
import com.drivaltech.finance.exception.ForbiddenException;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import com.drivaltech.finance.repository.CategoryRepository;
import com.drivaltech.finance.repository.TransactionRepository;
import com.drivaltech.finance.specification.TransactionSpecification;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public TransactionResponse create(CreateTransactionRequest request) {

        User user = getAuthenticatedUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        // Validação de ownership da categoria
        if (!category.getUser().getId().equals(user.getId())) {
            logger.warn("User {} tried to use category {} from another user",
                    user.getId(), category.getId());
            throw new ForbiddenException("You do not have permission to use this category");
        }

        try {
            Transaction transaction = new Transaction();
            transaction.setDescription(request.getDescription());
            transaction.setAmount(request.getAmount());
            transaction.setDate(request.getDate());
            transaction.setType(TransactionType.valueOf(request.getType()));
            transaction.setCategory(category);
            transaction.setUser(user);

            logger.info("Creating transaction | userId={} | amount={} | type={}",
                    user.getId(), request.getAmount(), request.getType());

            Transaction saved = transactionRepository.save(transaction);

            logger.info("Transaction created successfully | id={}", saved.getId());

            return TransactionResponse.fromEntity(saved);

        } catch (Exception e) {
            logger.error("Error creating transaction | userId={} | error={}",
                    user.getId(), e.getMessage());
            throw e;
        }
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {

        User user = getAuthenticatedUser();

        logger.info("Fetching all transactions | userId={}", user.getId());

        return transactionRepository
                .findAll(pageable)
                .map(TransactionResponse::fromEntity);
    }

    public TransactionResponse findById(UUID id) {

        User user = getAuthenticatedUser();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found with id: " + id)
                );

        boolean isOwner = transaction.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            logger.warn("Unauthorized access | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to access this transaction");
        }

        logger.info("Fetching transaction | id={}", id);

        return TransactionResponse.fromEntity(transaction);
    }

    public TransactionResponse update(UUID id, CreateTransactionRequest request) {

        User user = getAuthenticatedUser();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));

        boolean isOwner = transaction.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            logger.warn("Unauthorized update attempt | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to update this transaction");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        // Validação de ownership da categoria
        if (!category.getUser().getId().equals(user.getId())) {
            logger.warn("User {} tried to update transaction {} with foreign category {}",
                    user.getId(), id, category.getId());
            throw new ForbiddenException("You do not have permission to use this category");
        }

        try {
            transaction.setDescription(request.getDescription());
            transaction.setAmount(request.getAmount());
            transaction.setDate(request.getDate());
            transaction.setType(TransactionType.valueOf(request.getType()));
            transaction.setCategory(category);

            transactionRepository.save(transaction);

            logger.info("Transaction updated | id={}", id);

            return TransactionResponse.fromEntity(transaction);

        } catch (Exception e) {
            logger.error("Error updating transaction | id={} | error={}",
                    id, e.getMessage());
            throw e;
        }
    }

    public void delete(UUID id) {

        User user = getAuthenticatedUser();

        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));

        boolean isOwner = transaction.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            logger.warn("Unauthorized delete attempt | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to delete this transaction");
        }

        try {
            transactionRepository.delete(transaction);

            logger.warn("Transaction deleted | id={} | userId={}",
                    id, user.getId());

        } catch (Exception e) {
            logger.error("Error deleting transaction | id={} | error={}",
                    id, e.getMessage());
            throw e;
        }
    }

    public Page<TransactionResponse> findAllWithFilters(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            Pageable pageable
    ) {

        User user = getAuthenticatedUser();

        logger.info("Fetching transactions with filters | userId={}", user.getId());

        Specification<Transaction> spec =
                TransactionSpecification.withFilters(
                        type,
                        startDate,
                        endDate,
                        categoryId,
                        user
                );

        Page<Transaction> page = transactionRepository.findAll(spec, pageable);

        return page.map(TransactionResponse::fromEntity);
    }

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

    public PaginationResponse<TransactionResponse> findAll(
            int page,
            int size,
            String[] sort,
            String type
    ) {

        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";

        org.springframework.data.domain.Sort.Direction direction =
                sortDirection.equalsIgnoreCase("desc")
                        ? org.springframework.data.domain.Sort.Direction.DESC
                        : org.springframework.data.domain.Sort.Direction.ASC;

        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                page,
                size,
                org.springframework.data.domain.Sort.by(direction, sortField)
        );

        User user = getAuthenticatedUser();

        TransactionType transactionType = null;

        if (type != null) {
            try {
                transactionType = TransactionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid transaction type: " + type);
            }
        }

        Page<Transaction> result;

        if (transactionType != null) {
            result = transactionRepository.findByUserAndType(user, transactionType, pageable);
        } else {
            result = transactionRepository.findByUser(user, pageable);
        }

        logger.info("Fetching paginated transactions | userId={} | page={} | size={}",
                user.getId(), page, size);

        return new PaginationResponse<>(
                result.map(TransactionResponse::fromEntity)
        );
    }
}