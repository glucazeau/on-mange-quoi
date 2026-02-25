package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.dish.Dish;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MealPlan {

    @EqualsAndHashCode.Include
    @ToString.Include
    private final Week week;

    private final List<Day> days;

    public static MealPlan schoolWeek(Week week) {
        List<Day> days = List.of(
                Day.dinner(DayOfWeek.MONDAY),
                Day.dinner(DayOfWeek.TUESDAY),
                Day.lunchAndDinner(DayOfWeek.WEDNESDAY),
                Day.dinner(DayOfWeek.THURSDAY),
                Day.dinner(DayOfWeek.FRIDAY),
                Day.lunchAndDinner(DayOfWeek.SATURDAY),
                Day.lunchAndDinner(DayOfWeek.SUNDAY));
        return new MealPlan(week, days);
    }

    public static MealPlan from(Week week, MealPlanEntity mealPlanEntity) {
        Map<DayOfWeek, List<MealEntity>> mealsPerDay =
                mealPlanEntity.getMeals().stream().collect(Collectors.groupingBy(MealEntity::getDayOfWeek));
        List<Day> days = mealsPerDay.entrySet().stream()
                .map(e -> Day.from(e.getKey(), e.getValue()))
                .toList();
        return new MealPlan(week, days);
    }

    public Set<Dish> getDishes() {
        return days.stream()
                .map(Day::getMeals)
                .flatMap(Collection::stream)
                .map(Meal::getDish)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
