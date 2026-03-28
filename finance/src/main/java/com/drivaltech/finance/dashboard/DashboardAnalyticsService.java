package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.dto.DashboardAnalyticsResponse;
import com.drivaltech.finance.repository.TransactionRepository;
import com.drivaltech.finance.user.User;
import com.drivaltech.finance.user.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DashboardAnalyticsService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DashboardAnalyticsService(TransactionRepository transactionRepository,
                                     UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public DashboardAnalyticsResponse getAnalytics(LocalDate startDate, LocalDate endDate) {

        User user = getAuthenticatedUser();

        // 🔹 Período atual
        DashboardSummaryProjection current =
                transactionRepository.getSummaryByUserIdAndDate(
                        user.getId(),
                        startDate,
                        endDate,
                        null,
                        null
                );

        // 🔹 Calcular período anterior
        long days = ChronoUnit.DAYS.between(startDate, endDate);

        LocalDate previousStart = startDate.minusDays(days + 1);
        LocalDate previousEnd = endDate.minusDays(days + 1);

        // 🔹 Período anterior
        DashboardSummaryProjection previous =
                transactionRepository.getSummaryByUserIdAndDate(
                        user.getId(),
                        previousStart,
                        previousEnd,
                        null,
                        null
                );

        // 🔹 Normalizar valores
        BigDecimal currentIncome = getValue(current != null ? current.getIncome() : null);
        BigDecimal currentExpense = getValue(current != null ? current.getExpense() : null);
        BigDecimal currentBalance = currentIncome.subtract(currentExpense);

        BigDecimal previousIncome = getValue(previous != null ? previous.getIncome() : null);
        BigDecimal previousExpense = getValue(previous != null ? previous.getExpense() : null);
        BigDecimal previousBalance = previousIncome.subtract(previousExpense);

        // 🔹 Crescimento
        Double incomeGrowth = calculateGrowth(currentIncome, previousIncome);
        Double expenseGrowth = calculateGrowth(currentExpense, previousExpense);
        Double balanceGrowth = calculateGrowth(currentBalance, previousBalance);

        return new DashboardAnalyticsResponse(
                currentIncome,
                previousIncome,
                incomeGrowth,
                currentExpense,
                previousExpense,
                expenseGrowth,
                currentBalance,
                previousBalance,
                balanceGrowth
        );
    }

    // 🔥 Método para evitar null
    private BigDecimal getValue(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    // 🔥 Cálculo de crescimento
    private Double calculateGrowth(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    // 🔐 Usuário autenticado (igual ao DashboardService)
    private User getAuthenticatedUser() {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
