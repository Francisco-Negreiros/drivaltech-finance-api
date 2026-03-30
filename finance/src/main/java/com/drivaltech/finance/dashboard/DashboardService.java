package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.domain.TransactionType;
import com.drivaltech.finance.dto.DashboardSummaryResponse;
import com.drivaltech.finance.exception.BusinessException;
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
            UUID categoryId,
            String type) {

        User user = authService.getAuthenticatedUser();

        // Validação profissional do type
        TransactionType transactionType = null;

        if (type != null) {
            try {
                transactionType = TransactionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid transaction type: " + type);
            }
        }

        DashboardSummaryProjection projection =
                transactionRepository.getSummaryByUserIdAndDate(
                        user.getId(),
                        startDate,
                        endDate,
                        categoryId,
                        transactionType
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