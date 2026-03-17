package com.drivaltech.finance.repository;

import com.drivaltech.finance.dashboard.DashboardSummaryProjection;
import com.drivaltech.finance.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID>,
        JpaSpecificationExecutor<Transaction> {
    @Query("""
       SELECT
       COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END),0) AS income,
       COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END),0) AS expense
       FROM Transaction t
       """)
    DashboardSummaryProjection getDashboardSummary();
}