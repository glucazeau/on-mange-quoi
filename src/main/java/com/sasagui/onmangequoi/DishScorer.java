package com.sasagui.onmangequoi;

import java.util.List;

@FunctionalInterface
public interface DishScorer {

    int score(Dish dish, Meal meal, Week currentWeek, List<Week> previousWeeks);
}
