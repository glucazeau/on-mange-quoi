package com.sasagui.onmangequoi.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Week {

    @ToString.Include
    private final int year;

    @ToString.Include
    private final int number;

    private boolean current;

    private final LocalDate start;

    private final LocalDate end;

    private final WeekRef previousWeek;

    private final WeekRef nextWeek;

    private Season season;

    public record WeekRef(int year, int number) {}

    public boolean isOver() {
        return LocalDate.now().isAfter(end);
    }

    public static WeekBuilder builder() {
        return new WeekBuilder();
    }

    @Setter
    @Accessors(fluent = true, chain = true)
    public static class WeekBuilder {

        private int year;

        private int number;

        public Week build() {
            LocalDate firstDayOfWeek = getFirstDayOfWeek(year, number);
            LocalDate lastDayOfWeek = getLastDayOfWeek(year, number);

            LocalDate today = LocalDate.now();
            boolean currentWeek = !today.isBefore(firstDayOfWeek) && !today.isAfter(lastDayOfWeek);

            WeekRef previousWeek = getPreviousWeek(year, number);
            WeekRef nextWeek = getNextWeek(year, number);

            Season season = Season.fromMonth(firstDayOfWeek.getMonth());

            return new Week(year, number, currentWeek, firstDayOfWeek, lastDayOfWeek, previousWeek, nextWeek, season);
        }

        private LocalDate getFirstDayOfWeek(int year, long weekNumber) {
            return LocalDate.of(year, 1, 1)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .plusWeeks(weekNumber - 1);
        }

        private LocalDate getLastDayOfWeek(int year, long weekNumber) {
            return LocalDate.of(year, 1, 1)
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .plusWeeks(weekNumber - 1);
        }

        private WeekRef getPreviousWeek(int year, long weekNumber) {
            LocalDate firstDayOfGivenWeek = getFirstDayOfWeek(year, weekNumber);
            LocalDate firstDayOfPreviousWeek = firstDayOfGivenWeek.minusWeeks(1);

            int previousWeekNumber = firstDayOfPreviousWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
            int previousYear = firstDayOfPreviousWeek.get(WeekFields.ISO.weekBasedYear());

            return new WeekRef(previousYear, previousWeekNumber);
        }

        private WeekRef getNextWeek(int year, long weekNumber) {
            LocalDate firstDayOfGivenWeek = getFirstDayOfWeek(year, weekNumber);
            LocalDate firstDayOfNextWeek = firstDayOfGivenWeek.plusWeeks(1);

            int nextWeekNumber = firstDayOfNextWeek.get(WeekFields.ISO.weekOfWeekBasedYear());
            int nextWeekYear = firstDayOfNextWeek.get(WeekFields.ISO.weekBasedYear());

            return new WeekRef(nextWeekYear, nextWeekNumber);
        }
    }
}
