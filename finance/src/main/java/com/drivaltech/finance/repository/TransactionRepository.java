package com.drivaltech.finance.repository;

import com.drivaltech.finance.dashboard.DashboardSummaryProjection;
import com.drivaltech.finance.domain.Transaction;
import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID>,
        JpaSpecificationExecutor<Transaction> {

    Page<Transaction> findByUser(User user, Pageable pageable);

    Optional<Transaction> findByIdAndUser(UUID id, User user);

    @Query("""
    SELECT 
        SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END) AS income,
        SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END) AS expense
    FROM Transaction t
    WHERE t.user.id = :userId
    AND t.date >= COALESCE(:startDate, t.date)
    AND t.date <= COALESCE(:endDate, t.date)
    AND t.category.id = COALESCE(:categoryId, t.category.id)
    AND t.type = COALESCE(:type, t.type)
""")
   /* DashboardSummaryProjection getSummaryByUserIdAndDate(
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId,
            TransactionType transactionType);*/
    DashboardSummaryProjection getSummaryByUserIdAndDate(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("categoryId") UUID categoryId,
            @Param("type") TransactionType type
    );
}