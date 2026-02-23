package com.sasagui.onmangequoi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Meal {

    private final MealType type;

    @Setter
    private Dish dish;

    public static Meal from(MealEntity mealEntity) {
        Meal meal = new Meal(mealEntity.getMealType());
        meal.setDish(Dish.from(mealEntity.getDish()));
        return meal;
    }
}
