package com.drivaltech.finance.dto;

import java.math.BigDecimal;

public class DashboardAnalyticsResponse {

    private BigDecimal currentIncome;
    private BigDecimal previousIncome;
    private Double incomeGrowth;

    private BigDecimal currentExpense;
    private BigDecimal previousExpense;
    private Double expenseGrowth;

    private BigDecimal currentBalance;
    private BigDecimal previousBalance;
    private Double balanceGrowth;

    public DashboardAnalyticsResponse(
            BigDecimal currentIncome,
            BigDecimal previousIncome,
            Double incomeGrowth,
            BigDecimal currentExpense,
            BigDecimal previousExpense,
            Double expenseGrowth,
            BigDecimal currentBalance,
            BigDecimal previousBalance,
            Double balanceGrowth
    ) {
        this.currentIncome = currentIncome;
        this.previousIncome = previousIncome;
        this.incomeGrowth = incomeGrowth;
        this.currentExpense = currentExpense;
        this.previousExpense = previousExpense;
        this.expenseGrowth = expenseGrowth;
        this.currentBalance = currentBalance;
        this.previousBalance = previousBalance;
        this.balanceGrowth = balanceGrowth;
    }

    public BigDecimal getCurrentIncome() {
        return currentIncome;
    }

    public BigDecimal getPreviousIncome() {
        return previousIncome;
    }

    public Double getIncomeGrowth() {
        return incomeGrowth;
    }

    public BigDecimal getCurrentExpense() {
        return currentExpense;
    }

    public BigDecimal getPreviousExpense() {
        return previousExpense;
    }

    public Double getExpenseGrowth() {
        return expenseGrowth;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    public Double getBalanceGrowth() {
        return balanceGrowth;
    }
}
