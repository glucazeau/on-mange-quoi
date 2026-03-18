package com.sasagui.onmangequoi.calendar;

import com.sasagui.onmangequoi.meal.Meal;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.*;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Day implements Comparable<Day> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final DayOfWeek dayOfWeek;

    private LocalDate date;

    private final List<Meal> meals;

    private Day(DayOfWeek dayOfWeek, List<Meal> meals) {
        this.dayOfWeek = dayOfWeek;
        this.meals = meals;
    }

    private Day(Week week, DayOfWeek dayOfWeek, List<Meal> meals) {
        this(dayOfWeek, meals);
        this.date = week.getStart().plusDays((long) dayOfWeek.getValue() - 1);
    }

    public boolean isToday() {
        return LocalDate.now().equals(date);
    }

    public boolean isPast() {
        return LocalDate.now().isAfter(date);
    }

    public boolean isWeekend() {
        return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
    }

    public static Day from(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, Collections.emptyList());
    }

    public static Day from(Week week, DayOfWeek dayOfWeek) {
        return new Day(week, dayOfWeek, Collections.emptyList());
    }

    public static Day from(Week week, DayOfWeek dayOfWeek, List<Meal> meals) {
        return new Day(week, dayOfWeek, meals);
    }

    public List<Meal> getMeals() {
        return meals.stream().sorted().toList();
    }

    @Override
    public int compareTo(@NotNull Day o) {
        return this.dayOfWeek.compareTo(o.getDayOfWeek());
    }
}
