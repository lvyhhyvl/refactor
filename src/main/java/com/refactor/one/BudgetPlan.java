package com.refactor.one;


public class BudgetPlan {
    private final BudgetRepo repo;

    public BudgetPlan(BudgetRepo repo) {
        this.repo = repo;
    }

    public long query(Period period) {
        // If the area between Start and End overlap at least two budget periods.

           return repo.findAll().stream().mapToLong(budget -> budget.getOverlappingAmount(period)).sum();
    }


}
