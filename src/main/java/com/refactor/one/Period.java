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

    long getDays() {
        return start.isAfter(end) ? 0 : start.until(end, DAYS) + 1;
    }

    long getOverlappingDays(Period another) {

        LocalDate endOfOverlapping = end.isBefore(another.end) ? end : another.end;
        LocalDate startOfOverlapping = start.isAfter(another.start) ? start : another.start;
        return new Period(startOfOverlapping, endOfOverlapping).getDays();
    }
}
