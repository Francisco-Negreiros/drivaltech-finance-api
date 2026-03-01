package com.drivaltech.finance.repository;

import com.drivaltech.finance.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID>,
        JpaSpecificationExecutor<Transaction> {
}