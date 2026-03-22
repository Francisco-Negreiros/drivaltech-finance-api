package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.dto.DashboardSummaryResponse;
import com.drivaltech.finance.repository.TransactionRepository;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DashboardService(TransactionRepository transactionRepository,
                            UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummaryResponse getSummary(
            LocalDate startDate,
            LocalDate endDate
    ) {

        User user = getAuthenticatedUser();

        DashboardSummaryProjection projection =
                transactionRepository.getSummaryByUserIdAndDate(
                        user.getId(),
                        startDate,
                        endDate
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
