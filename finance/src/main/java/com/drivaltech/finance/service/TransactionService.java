package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Category;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.CreateTransactionRequest;
import com.drivaltech.finance.dto.TransactionResponse;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import com.drivaltech.finance.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            CategoryRepository categoryRepository) {

        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
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
}
