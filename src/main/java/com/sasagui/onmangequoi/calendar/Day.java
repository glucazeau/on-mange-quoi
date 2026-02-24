package com.sasagui.onmangequoi.calendar;

import com.sasagui.onmangequoi.meal.Meal;
import com.sasagui.onmangequoi.meal.MealEntity;
import com.sasagui.onmangequoi.meal.MealType;
import java.time.DayOfWeek;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
public class Day {

    @ToString.Include
    private final DayOfWeek dayOfWeek;

    private final List<Meal> meals;

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
