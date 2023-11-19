package com.refactor.one;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BudgetPlanTest {
    private final BudgetRepo repo = mock(BudgetRepo.class);
    private final BudgetPlan plan = new BudgetPlan(repo);

    @Test
    public void noBudget() {
        givenBudgets();
        assertEquals(0, plan.query(LocalDate.of(2019, 10, 4), LocalDate.of(2019, 11, 5)));
    }

    @Test
    public void queryWholeMonth() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(3100, plan.query(LocalDate.of(2019, 10, 1), LocalDate.of(2019, 10, 31)));
    }

    @Test
    public void queryOneDayWithinOneMonth() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(100, plan.query(LocalDate.of(2019, 10, 3), LocalDate.of(2019, 10, 3)));
    }

    @Test
    public void queryTwoDayWithinOneMonth() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(200, plan.query(LocalDate.of(2019, 10, 3), LocalDate.of(2019, 10, 4)));
    }

    @Test
    public void queryBeforeBudget() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(400, plan.query(LocalDate.of(2019, 9, 25), LocalDate.of(2019, 10, 4)));
    }

    @Test
    public void queryAfterBudget() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(400, plan.query(LocalDate.of(2019, 10, 28), LocalDate.of(2019, 11, 4)));
    }

    @Test
    public void queryOutOfBudget() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100));
        assertEquals(0, plan.query(LocalDate.of(2019, 9, 1), LocalDate.of(2019, 9, 24)));
    }

    @Test
    public void queryMultiBudget() {
        givenBudgets(new Budget(YearMonth.of(2019, 10), 3100),
                new Budget(YearMonth.of(2019, 11), 3000));
        assertEquals(2000, plan.query(LocalDate.of(2019, 10, 20), LocalDate.of(2019, 11, 8)));
    }

    private void givenBudgets(Budget... budgets) {
        when(repo.findAll()).thenReturn(Arrays.asList(budgets));
    }
}
