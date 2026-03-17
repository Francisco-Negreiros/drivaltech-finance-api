package com.drivaltech.finance.dto;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;

    public DashboardSummaryResponse(
            BigDecimal income,
            BigDecimal expense,
            BigDecimal balance) {

        this.income = income;
        this.expense = expense;
        this.balance = balance;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
