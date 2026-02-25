package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.meal.Meal;
import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface DishScorer {

    int score(Dish dish, Day day, Meal meal, Set<Dish> currentWeekDishes, List<Dish> previousWeeksDishes);
}
