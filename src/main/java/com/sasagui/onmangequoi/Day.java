package com.sasagui.onmangequoi;

import java.time.DayOfWeek;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Day {

    private final DayOfWeek dayOfWeek;

    private final List<Meal> meals;

    public static Day dinner(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, List.of(new Meal(MealType.DINNER)));
    }

    public static Day lunchAndDinner(DayOfWeek dayOfWeek) {
        return new Day(dayOfWeek, List.of(new Meal(MealType.LUNCH), new Meal(MealType.DINNER)));
    }
}
