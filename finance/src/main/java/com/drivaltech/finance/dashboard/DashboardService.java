package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.DashboardSummaryResponse;
import com.drivaltech.finance.repository.TransactionRepository;
import com.drivaltech.finance.user.User;
import org.springframework.stereotype.Service;
import com.drivaltech.finance.service.AuthService;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final AuthService authService;

    public DashboardService(TransactionRepository transactionRepository,
                            AuthService authService) {
        this.transactionRepository = transactionRepository;
        this.authService = authService;
    }

    public DashboardSummaryResponse getSummary(
            LocalDate startDate,
            LocalDate endDate,
            UUID categoryId, TransactionType type) {

        User user = authService.getAuthenticatedUser();

        DashboardSummaryProjection projection =
                transactionRepository.getSummaryByUserIdAndDate(
                        user.getId(),
                        startDate,
                        endDate,
                        categoryId,
                        type
                );

        if (projection == null) {
            return new DashboardSummaryResponse(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        BigDecimal income = projection.getIncome() != null
                ? projection.getIncome()
                : BigDecimal.ZERO;

        BigDecimal expense = projection.getExpense() != null
                ? projection.getExpense()
                : BigDecimal.ZERO;

        BigDecimal balance = income.subtract(expense);

        return new DashboardSummaryResponse(income, expense, balance);
    }
}
