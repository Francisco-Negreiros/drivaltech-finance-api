package com.drivaltech.finance.dashboard;

import com.drivaltech.finance.dto.DashboardSummaryResponse;
import com.drivaltech.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardSummaryResponse getSummary() {

        DashboardSummaryProjection summary =
                transactionRepository.getDashboardSummary();

        BigDecimal income = summary.getIncome();
        BigDecimal expense = summary.getExpense();

        BigDecimal balance = income.subtract(expense);

        return new DashboardSummaryResponse(
                income,
                expense,
                balance
        );
    }
}
