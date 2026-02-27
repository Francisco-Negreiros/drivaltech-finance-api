package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.exception.BusinessException;
import com.drivaltech.finance.dto.TransactionResponse;
import com.drivaltech.finance.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
}
