package com.drivaltech.finance.service;

import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.exception.BusinessException;
import com.drivaltech.finance.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction create(Transaction transaction) {

        // Primeira regra de negócio
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transaction amount must be greater than zero");
        }

        return transactionRepository.save(transaction);
    }

    public Page<TransactionResponse> findAll(Pageable pageable) {

        return transactionRepository
                .findAll(pageable)
                .map(TransactionResponse::fromEntity);
    }
}
