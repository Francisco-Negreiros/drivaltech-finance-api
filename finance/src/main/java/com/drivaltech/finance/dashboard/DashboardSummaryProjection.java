package com.drivaltech.finance.dashboard;

import java.math.BigDecimal;

public interface DashboardSummaryProjection {

    BigDecimal getIncome();

    BigDecimal getExpense();
}
