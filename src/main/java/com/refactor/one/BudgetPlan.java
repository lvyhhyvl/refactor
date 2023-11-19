package com.refactor.one;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class BudgetPlan {
    private final BudgetRepo repo;

    public BudgetPlan(BudgetRepo repo) {
        this.repo = repo;
    }

    public long query(Period period) {
        //If Start and End are in the same budget period
        Budget firstBudget = getBudgetContaining(period.getStart());
        Budget lastBudget = getBudgetContaining(period.getEnd());
        if (firstBudget.equals(lastBudget)) {
            long amountBetween = getAmountBetween(new Period(period.getStart(), period.getEnd()), firstBudget);
            return amountBetween;
        }

        // If the area between Start and End overlap at least two budget periods.
        if (firstBudget.getMonth().isBefore(lastBudget.getMonth())) {
            long totalStartPeriod = getAmountBetween(new Period(period.getStart(), firstBudget.getEnd()), firstBudget);

            long totalInMiddle = 0;
            for (Budget budget : getBudgetBetween(period.getStart(), period.getEnd())) {
                totalInMiddle += budget.getAmount();
            }

            long totalEndPeriod = getAmountBetween(new Period(lastBudget.getStart(), period.getEnd()), lastBudget);

            return totalStartPeriod + totalInMiddle + totalEndPeriod;
        }

        throw new RuntimeException("You should not be here.");
    }

    private long getAmountBetween(Period period, Budget budget) {
        long amount = budget.getAmount();
        long daysInPeriod = budget.getDays();
        long daysBetween = period.getDays();
        long amountBetween = amount / daysInPeriod * daysBetween;
        return amountBetween;
    }

    private long getBudgetDaysCount(LocalDate date) {
        Budget budget = getBudgetContaining(date);
        return budget.getDays();
    }

    private Budget getBudgetContaining(LocalDate date) {
        List<Budget> budgets = repo.findAll();
        return budgets.stream()
                .filter(budget -> budget.getStart().equals(date.withDayOfMonth(1)))
                .findFirst()
                .orElse(new Budget(YearMonth.of(date.getYear(), date.getMonth()), 0));
    }

    private long getBudgetAmount(LocalDate date) {
        Budget budget = getBudgetContaining(date);
        return budget.getAmount();
    }

    private List<Budget> getBudgetBetween(LocalDate startDate, LocalDate endDate) {
        List<Budget> budgets = repo.findAll();
        return budgets.stream()
                .filter(budget -> budget.getStart().isAfter(startDate) && budget.getEnd().isBefore(endDate))
                .collect(Collectors.toList());
    }
}
