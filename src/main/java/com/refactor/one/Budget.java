package com.refactor.one;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class Budget {
    private final YearMonth month;
    private final long amount;

    public Budget(YearMonth month, long amount) {
        this.month = month;
        this.amount = amount;
    }

    public YearMonth getMonth() {
        return month;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        Budget budget = (Budget) o;
        return Objects.equals(month, budget.month);
    }

    LocalDate getEnd() {
        return month.atEndOfMonth();
    }

    LocalDate getStart() {
        return month.atDay(1);
    }

    long getDays() {
        return month.lengthOfMonth();
    }

    public Period getPeriod() {
        return new Period(getStart(),getEnd());
    }

    long getOverlappingAmount(Period period) {
        long daysBetween = period.getOverlappingDays(getPeriod());
        return this.amount / getDays() * daysBetween;
    }
}
