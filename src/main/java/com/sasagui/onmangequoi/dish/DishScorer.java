package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.meal.Meal;
import java.util.List;

@FunctionalInterface
public interface DishScorer {

    int score(Dish dish, Meal meal, Week currentWeek, List<Week> previousWeeks);
}
