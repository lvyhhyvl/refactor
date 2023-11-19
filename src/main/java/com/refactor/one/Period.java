package com.refactor.one;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    public Period(LocalDate start, LocalDate endDate) {
        this.start = start;
        this.end = endDate;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    long getDays() {
        return start.until(end, DAYS) + 1;
    }
}
