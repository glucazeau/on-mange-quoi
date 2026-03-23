package com.sasagui.onmangequoi.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Season {
    WINTER(MonthDay.of(12, 21), MonthDay.of(3, 19)),
    SPRING(MonthDay.of(3, 20), MonthDay.of(6, 20)),
    SUMMER(MonthDay.of(6, 21), MonthDay.of(9, 22)),
    AUTUMN(MonthDay.of(9, 23), MonthDay.of(12, 20));

    private final MonthDay start;

    private final MonthDay end;

    public static Season fromDate(LocalDate date) {
        MonthDay monthDay = MonthDay.from(date);
        for (Season season : Season.values()) {
            if (season.isDateInSeason(monthDay, season)) {
                return season;
            }
        }
        return WINTER;
    }

    private boolean isDateInSeason(MonthDay monthDay, Season season) {
        return (monthDay.equals(season.start) || monthDay.isAfter(season.start))
                && (monthDay.equals(season.end) || monthDay.isBefore(season.end));
    }
}
