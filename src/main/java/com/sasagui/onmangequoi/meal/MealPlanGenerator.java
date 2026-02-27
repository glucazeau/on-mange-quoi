package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.dish.Dish;
import com.sasagui.onmangequoi.dish.DishSelector;
import com.sasagui.onmangequoi.dish.DishService;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MealPlanGenerator {

    private final DishService dishService;

    private final DishSelector dishSelector;

    public MealPlan generateMealPlan(Week week) {
        log.info("Generating new meal plan for {}", week);
        List<Dish> dishes = dishService.listDishes(null);
        MealPlan mealPlan = MealPlan.schoolWeek(week);

        for (Day day : mealPlan.getDays()) {
            for (Meal meal : day.getMeals()) {
                Dish selectedDish =
                        dishSelector.selectDish(dishes, day, meal, mealPlan.getDishes(), Collections.emptyList());
                meal.setDish(selectedDish);
                dishes.remove(selectedDish);
            }
        }
        return mealPlan;
    }
}
