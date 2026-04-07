package com.drivaltech.finance.dto;

import java.io.Serializable;

import java.math.BigDecimal;

public class DashboardSummaryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;

    public DashboardSummaryResponse (
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
