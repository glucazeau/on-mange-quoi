package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import com.sasagui.onmangequoi.dish.Dish;
import com.sasagui.onmangequoi.dish.DishSearchCriteria;
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

        log.info("Loading dishes available for current month");
        List<Dish> dishes = dishService.listDishes(DishSearchCriteria.currentMonth());

        log.info("Checking if some meals are already planned for week {}", week);
        MealPlan mealPlan = mealPlanService.getMealPlan(week);
        Set<Dish> alreadyUsedDishes = mealPlan.getDishes();
        log.info("Removing {} already used dishes from available dishes list", alreadyUsedDishes.size());
        alreadyUsedDishes.forEach(dishes::remove);

        log.info("Loading previous week dishes");
        Week previousWeek = weekService.getPreviousWeek(week);
        MealPlan previousWeekMealPlan = mealPlanService.getMealPlan(previousWeek);

        Set<Dish> olderWeeksDishes = getOlderWeeksDishes(previousWeek);

        Set<Meal> mealsToPlan =
                mealPlan.getMeals().stream().filter(Meal::isEmpty).collect(Collectors.toSet());
        log.info("Going to plan {} meals", mealsToPlan.size());
        for (Meal meal : mealsToPlan) {
            log.info("Planning meal {}", meal);
            Dish selectedDish = dishSelector.selectDish(
                    dishes,
                    meal.getDayOfWeek(),
                    meal,
                    mealPlan.getDishes(),
                    previousWeekMealPlan.getDishes(),
                    olderWeeksDishes);
            meal.setDish(selectedDish);
            dishes.remove(selectedDish);
        }
        return mealPlan;
    }

    private Set<Dish> getOlderWeeksDishes(Week previousWeek) {
        log.info("Loading dishes used during the three weeks before");
        List<Week> previousWeeks = weekService.getPreviousWeeks(previousWeek, 3);
        return previousWeeks.stream()
                .map(week -> mealPlanService.getMealPlan(week).getDishes())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
