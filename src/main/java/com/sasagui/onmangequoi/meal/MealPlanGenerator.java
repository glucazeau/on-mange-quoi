package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import com.sasagui.onmangequoi.dish.Dish;
import com.sasagui.onmangequoi.dish.DishSelector;
import com.sasagui.onmangequoi.dish.DishService;
import java.util.*;
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

        log.info("Loading previous week meal plan");
        Week previousWeek = weekService.getPreviousWeek(week);
        Optional<MealPlan> previousWeekMealPlan = mealPlanService.getMealPlan(previousWeek);

        log.info("Loading two weeks before meal plans");
        Set<Dish> olderWeeksDishes = getOlderWeeksDishes(previousWeek);

        MealPlan mealPlan = MealPlan.schoolWeek(week);

        for (Day day : mealPlan.getDays()) {
            for (Meal meal : day.getMeals()) {
                Dish selectedDish = dishSelector.selectDish(
                        dishes,
                        day,
                        meal,
                        previousWeekMealPlan.map(MealPlan::getDishes).orElse(Collections.emptySet()),
                        olderWeeksDishes);
                meal.setDish(selectedDish);
                dishes.remove(selectedDish);
            }
        }
        return mealPlan;
    }

    private Set<Dish> getOlderWeeksDishes(Week previousWeek) {
        Week firstWeekBefore = weekService.getWeek(
                previousWeek.getPreviousWeek().year(),
                previousWeek.getPreviousWeek().number());
        Optional<MealPlan> firstWeekBeforeMealPlan = mealPlanService.getMealPlan(firstWeekBefore);
        Set<Dish> firstWeekBeforeDishes =
                firstWeekBeforeMealPlan.map(MealPlan::getDishes).orElse(Collections.emptySet());

        Week secondWeekBefore = weekService.getWeek(
                firstWeekBefore.getPreviousWeek().year(),
                firstWeekBefore.getPreviousWeek().number());
        Optional<MealPlan> secondWeekBeforeMealPlan = mealPlanService.getMealPlan(secondWeekBefore);
        Set<Dish> secondWeekBeforeDishes =
                secondWeekBeforeMealPlan.map(MealPlan::getDishes).orElse(Collections.emptySet());

        Set<Dish> result = new HashSet<>(firstWeekBeforeDishes);
        result.addAll(secondWeekBeforeDishes);
        return result;
    }
}
