package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import com.sasagui.onmangequoi.dish.Dish;
import com.sasagui.onmangequoi.dish.DishSelector;
import com.sasagui.onmangequoi.dish.DishService;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MealPlanGenerator {

    private final WeekService weekService;

    private final DishService dishService;

    private final MealPlanService mealPlanService;

    private final DishSelector dishSelector;

    public MealPlan generateMealPlan(Week week) {
        log.info("Generating new meal plan for {}", week);
        List<Dish> dishes = dishService.listDishes(null);

        log.info("Loading previous week dishes");
        Week previousWeek = weekService.getPreviousWeek(week);
        MealPlan previousWeekMealPlan = mealPlanService.getMealPlan(previousWeek);

        Set<Dish> olderWeeksDishes = getOlderWeeksDishes(previousWeek);

        MealPlan mealPlan = MealPlan.schoolWeek(week);
        for (Meal meal : mealPlan.getMeals()) {
            Dish selectedDish = dishSelector.selectDish(
                    dishes, meal.getDayOfWeek(), meal, previousWeekMealPlan.getDishes(), olderWeeksDishes);
            meal.setDish(selectedDish);
            dishes.remove(selectedDish);
        }
        return mealPlan;
    }

    private Set<Dish> getOlderWeeksDishes(Week previousWeek) {
        log.info("Loading dishes used in the three weeks before");
        List<Week> previousWeeks = weekService.getPreviousWeeks(previousWeek, 3);
        return previousWeeks.stream()
                .map(week -> mealPlanService.getMealPlan(week).getDishes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
