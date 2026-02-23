package com.sasagui.onmangequoi.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Week {

    @ToString.Include
    private final Year year;

    @ToString.Include
    private final int number;

    private final LocalDate start;

    private final LocalDate end;

    private final WeekRef previousWeek;

    private final WeekRef nextWeek;

    private Season season;

    public record WeekRef(Year year, int number) {}

    public static WeekBuilder builder() {
        return new WeekBuilder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    public static class WeekBuilder {

        private Year year;

        private int number;

        public Week build() {
            LocalDate firstDayOfWeek = getFirstDayOfWeek(year, number);
            LocalDate lastDayOfWeek = getLastDayOfWeek(year, number);

            WeekRef previousWeek = getPreviousWeek(year, number);
            WeekRef nextWeek = getNextWeek(year, number);

            Season season = Season.fromMonth(firstDayOfWeek.getMonth());

            return new Week(year, number, firstDayOfWeek, lastDayOfWeek, previousWeek, nextWeek, season);
        }

        private LocalDate getFirstDayOfWeek(Year year, long weekNumber) {
            return LocalDate.of(year.getValue(), 1, 1)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .plusWeeks(weekNumber - 1);
        }

        private LocalDate getLastDayOfWeek(Year year, long weekNumber) {
            return LocalDate.of(year.getValue(), 1, 1)
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .plusWeeks(weekNumber - 1);
        }

        private WeekRef getPreviousWeek(Year year, long weekNumber) {
            LocalDate firstDayOfGivenWeek = getFirstDayOfWeek(year, weekNumber);
            LocalDate firstDayOfPreviousWeek = firstDayOfGivenWeek.minusWeeks(1);

            int previousWeekNumber = firstDayOfPreviousWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
            int previousYear = firstDayOfPreviousWeek.get(WeekFields.ISO.weekBasedYear());

            return new WeekRef(Year.of(previousYear), previousWeekNumber);
        }

        private WeekRef getNextWeek(Year year, long weekNumber) {
            LocalDate firstDayOfGivenWeek = getFirstDayOfWeek(year, weekNumber);
            LocalDate firstDayOfNextWeek = firstDayOfGivenWeek.plusWeeks(1);

            int nextWeekNumber = firstDayOfNextWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
            int nextWeekYear = firstDayOfNextWeek.get(WeekFields.ISO.weekBasedYear());

            return new WeekRef(Year.of(nextWeekYear), nextWeekNumber);
        }
    }
}
