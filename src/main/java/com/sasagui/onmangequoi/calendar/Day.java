package com.sasagui.onmangequoi.calendar;

import com.sasagui.onmangequoi.meal.Meal;
import com.sasagui.onmangequoi.meal.MealEntity;
import com.sasagui.onmangequoi.meal.MealType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
public class Day {

    @ToString.Include
    private final DayOfWeek dayOfWeek;

    private final List<Meal> meals;

    @Setter
    private LocalDate date;

    public boolean isToday() {
        return LocalDate.now().equals(date);
    }

    public static Day dinner(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, List.of(new Meal(MealType.DINNER)));
    }

    public static Day lunchAndDinner(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, List.of(new Meal(MealType.LUNCH), new Meal(MealType.DINNER)));
    }

    public static Day from(DayOfWeek dayOfWeek, List<MealEntity> meals) {
        return new Day(dayOfWeek, meals.stream().map(Meal::from).toList());
    }
}
