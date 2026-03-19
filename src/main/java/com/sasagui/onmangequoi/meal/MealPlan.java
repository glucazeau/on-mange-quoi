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

    @JsonIgnore
    private final List<Meal> meals;

    public static MealPlan schoolWeek(Week week) {
        return new MealPlan(week, getSchoolWeekMeals());
    }

    public static MealPlan from(Week week, List<MealEntity> mealEntities) {
        List<Meal> finalMeals = new ArrayList<>(getSchoolWeekMeals());
        for (MealEntity mealEntity : mealEntities) {
            Meal meal = Meal.from(mealEntity);
            finalMeals.remove(meal);
            finalMeals.add(meal);
        }
        return new MealPlan(week, finalMeals);
    }

    public List<Day> getDays() {
        Map<DayOfWeek, List<Meal>> mealsPerDay = meals.stream().collect(Collectors.groupingBy(Meal::getDayOfWeek));
        return mealsPerDay.entrySet().stream()
                .map(e -> Day.from(week, e.getKey(), e.getValue()))
                .sorted()
                .toList();
    }

    @JsonIgnore
    public Set<Dish> getDishes() {
        return meals.stream().map(Meal::getDish).collect(Collectors.toSet());
    }

    public boolean isEditable() {
        return !week.isInPast();
    }

    private static List<Meal> getSchoolWeekMeals() {
        return List.of(
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
    }
}
