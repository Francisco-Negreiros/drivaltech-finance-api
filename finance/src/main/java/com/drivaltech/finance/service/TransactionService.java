package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.CreateTransactionRequest;
import com.drivaltech.finance.dto.TransactionResponse;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import com.drivaltech.finance.repository.CategoryRepository;
import com.drivaltech.finance.specification.TransactionSpecification;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public TransactionResponse create(CreateTransactionRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(TransactionType.valueOf(request.getType()));
        transaction.setCategory(category);

        User user = getAuthenticatedUser();
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);

        return TransactionResponse.fromEntity(saved);
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {

        return transactionRepository
                .findAll(pageable)
                .map(TransactionResponse::fromEntity);
    }
    public TransactionResponse findById(UUID id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found with id: " + id)
                );

        return TransactionResponse.fromEntity(transaction);
    }
    public TransactionResponse update(UUID id, CreateTransactionRequest request) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(TransactionType.valueOf(request.getType()));
        transaction.setCategory(category);

        transactionRepository.save(transaction);

        return TransactionResponse.fromEntity(transaction);
    }
    public void delete(UUID id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transaction not found with id: " + id));

        transactionRepository.delete(transaction);
    }
    public Page<TransactionResponse> findAllWithFilters(
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            Pageable pageable
    ) {

        User user = getAuthenticatedUser();

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
}
