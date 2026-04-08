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
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;

import org.slf4j.MDC;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            AuditService auditService) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }
    @CacheEvict(value = "dashboard", allEntries = true)
    public TransactionResponse create(CreateTransactionRequest request) {

        User user = getAuthenticatedUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        if (!category.getUser().getId().equals(user.getId())) {
            log.warn("Forbidden category usage | userId={} | categoryId={}",
                    user.getId(), category.getId());
            throw new ForbiddenException("You do not have permission to use this category");
        }

        try {
            log.info("Creating transaction | userId={} | amount={} | type={}",
                    user.getId(), request.getAmount(), request.getType());

            Transaction transaction = new Transaction();
            transaction.setDescription(request.getDescription());
            transaction.setAmount(request.getAmount());
            transaction.setDate(request.getDate());
            transaction.setType(TransactionType.valueOf(request.getType()));
            transaction.setCategory(category);
            transaction.setUser(user);

            Transaction saved = transactionRepository.save(transaction);

            try {
                auditService.log(
                        user.getId(),
                        "CREATED",
                        "TRANSACTION",
                        saved.getId(),
                        MDC.get("ip")
                );
            } catch (Exception e) {
                log.error("Audit log failed | error={}", e.getMessage());
            }

            log.info("Transaction created | id={} | userId={}",
                    saved.getId(), user.getId());

            return TransactionResponse.fromEntity(saved);

        } catch (Exception e) {
            log.error("Error creating transaction | userId={}", user.getId(), e);
            throw e;
        }
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {

        User user = getAuthenticatedUser();

        log.info("Fetching all transactions | userId={}", user.getId());

        return transactionRepository
                .findAll(pageable)
                .map(TransactionResponse::fromEntity);
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

        log.info("Fetching paginated transactions | userId={} | page={} | size={}",
                user.getId(), page, size);

        return new PaginationResponse<>(
                result.map(TransactionResponse::fromEntity)
        );
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
            log.warn("Unauthorized access | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to access this transaction");
        }

        log.info("Fetching transaction | id={} | userId={}", id, user.getId());

        return TransactionResponse.fromEntity(transaction);
    }
    @CacheEvict(value = "dashboard", allEntries = true)
    public TransactionResponse update(UUID id, CreateTransactionRequest request) {

        User user = getAuthenticatedUser();

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));

        boolean isOwner = transaction.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            log.warn("Unauthorized update | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to update this transaction");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        if (!category.getUser().getId().equals(user.getId())) {
            log.warn("Forbidden category update | userId={} | transactionId={} | categoryId={}",
                    user.getId(), id, category.getId());
            throw new ForbiddenException("You do not have permission to use this category");
        }

        try {
            log.info("Updating transaction | id={} | userId={}", id, user.getId());

            transaction.setDescription(request.getDescription());
            transaction.setAmount(request.getAmount());
            transaction.setDate(request.getDate());
            transaction.setType(TransactionType.valueOf(request.getType()));
            transaction.setCategory(category);

            transactionRepository.save(transaction);

            try {
                auditService.log(
                        user.getId(),
                        "UPDATED",
                        "TRANSACTION",
                        transaction.getId(),
                        MDC.get("ip")
                );
            } catch (Exception e) {
                log.error("Audit log failed | error={}", e.getMessage());
            }

            log.info("Transaction updated | id={} | userId={}", id, user.getId());

            return TransactionResponse.fromEntity(transaction);

        } catch (Exception e) {
            log.error("Error updating transaction | id={} | userId={}", id, user.getId(), e);
            throw e;
        }
    }
    @CacheEvict(value = "dashboard", allEntries = true)
    public void delete(UUID id) {

        User user = getAuthenticatedUser();

        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));

        boolean isOwner = transaction.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            log.warn("Unauthorized delete | userId={} | transactionId={}",
                    user.getId(), id);
            throw new ForbiddenException("You do not have permission to delete this transaction");
        }

        try {
            log.info("Deleting transaction | id={} | userId={}", id, user.getId());

            transactionRepository.delete(transaction);

            try {
                auditService.log(
                        user.getId(),
                        "DELETED",
                        "TRANSACTION",
                        id,
                        MDC.get("ip")
                );
            } catch (Exception e) {
                log.error("Audit log failed | error={}", e.getMessage());
            }

            log.warn("Transaction deleted | id={} | userId={}", id, user.getId());

        } catch (Exception e) {
            log.error("Error deleting transaction | id={} | userId={}", id, user.getId(), e);
            throw e;
        }
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
}