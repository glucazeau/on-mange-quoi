package com.sasagui.onmangequoi.calendar;

import com.sasagui.onmangequoi.meal.Meal;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Day implements Comparable<Day> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final DayOfWeek dayOfWeek;

    private final List<Meal> meals;

    @Setter
    private LocalDate date;

    public boolean isToday() {
        return LocalDate.now().equals(date);
    }

    public boolean isWeekend() {
        return List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
    }

    public static Day from(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, Collections.emptyList());
    }

    public static Day from(DayOfWeek dayOfWeek, List<Meal> meals) {
        return new Day(dayOfWeek, meals);
    }

    public List<Meal> getMeals() {
        return meals.stream().sorted().toList();
    }

    @Override
    public int compareTo(@NotNull Day o) {
        return this.dayOfWeek.compareTo(o.getDayOfWeek());
    }
}
