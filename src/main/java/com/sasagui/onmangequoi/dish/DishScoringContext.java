package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.meal.Meal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DishScoringContext {
    Dish dish;

    Day day;

    Meal meal;

    Set<Dish> lastWeekDishes;

    Set<Dish> previousWeeksDishes;
}
