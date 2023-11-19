package com.refactor.one;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class BudgetPlan {
    private final BudgetRepo repo;

    public BudgetPlan(BudgetRepo repo) {
        this.repo = repo;
    }

    public long query(Period period) {
        //If Start and End are in the same budget period
        if (period.getStartDate().withDayOfMonth(1).equals(period.getEndDate().withDayOfMonth(1))) {
            long amountBetween = getAmountBetween(period.getStartDate(), period.getEndDate());
            return amountBetween;
        }

        // If the area between Start and End overlap at least two budget periods.
        if (YearMonth.from(period.getStartDate()).isBefore(YearMonth.from(period.getEndDate()))) {
            LocalDate endOfFirstBudget = period.getStartDate().withDayOfMonth(period.getStartDate().lengthOfMonth());
            long totalStartPeriod = getAmountBetween(period.getStartDate(), endOfFirstBudget);

            long totalInMiddle = 0;
            for (Budget budget : getBudgetBetween(period.getStartDate(), period.getEndDate())) {
                totalInMiddle += budget.getAmount();
            }

            LocalDate startOfLastBudget = period.getEndDate().withDayOfMonth(1);
            long totalEndPeriod = getAmountBetween(startOfLastBudget, period.getEndDate());

            return totalStartPeriod + totalInMiddle + totalEndPeriod;
        }

        throw new RuntimeException("You should not be here.");
    }

    private long getAmountBetween(LocalDate startDate, LocalDate endDate) {
        long amount = getBudgetAmount(startDate);
        long daysInPeriod = getBudgetDaysCount(startDate);
        long daysBetween = startDate.until(endDate, DAYS) + 1;
        long amountBetween = amount / daysInPeriod * daysBetween;
        return amountBetween;
    }

    private long getBudgetDaysCount(LocalDate date) {
        Budget budget = getBudgetContaining(date);
        return budget.getMonth().lengthOfMonth();
    }

    private Budget getBudgetContaining(LocalDate date) {
        List<Budget> budgets = repo.findAll();
        return budgets.stream()
                .filter(budget -> budget.getMonth().atDay(1).equals(date.withDayOfMonth(1)))
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
                .filter(budget -> budget.getMonth().atDay(1).isAfter(startDate) && budget.getMonth().atEndOfMonth().isBefore(endDate))
                .collect(Collectors.toList());
    }
}
