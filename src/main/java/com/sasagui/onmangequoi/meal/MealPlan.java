package com.sasagui.onmangequoi.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private final List<Meal> meals;

    public static MealPlan schoolWeek(Week week) {
        List<Meal> meals = List.of(
                Meal.empty(DayOfWeek.MONDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.TUESDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.WEDNESDAY, MealType.LUNCH),
                Meal.empty(DayOfWeek.WEDNESDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.THURSDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.FRIDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.SATURDAY, MealType.LUNCH),
                Meal.empty(DayOfWeek.SATURDAY, MealType.DINNER),
                Meal.empty(DayOfWeek.SUNDAY, MealType.LUNCH),
                Meal.empty(DayOfWeek.SUNDAY, MealType.DINNER));
        return new MealPlan(week, meals);
    }

    public static MealPlan from(Week week, List<MealEntity> meals) {
        return new MealPlan(week, meals.stream().map(Meal::from).toList());
    }

    public List<Day> getDays() {
        Map<DayOfWeek, List<Meal>> mealsPerDay = meals.stream().collect(Collectors.groupingBy(Meal::getDayOfWeek));
        List<Day> days = mealsPerDay.entrySet().stream()
                .map(e -> Day.from(e.getKey(), e.getValue()))
                .sorted()
                .toList();
        setDates(week, days);
        return days;
    }

    @JsonIgnore
    public Set<Dish> getDishes() {
        return meals.stream().map(Meal::getDish).collect(Collectors.toSet());
    }

    public boolean isEditable() {
        return !week.isInPast();
    }

    private static void setDates(Week week, List<Day> days) {
        for (Day day : days) {
            day.setDate(week.getStart().plusDays((long) day.getDayOfWeek().getValue() - 1));
        }
    }
}
