package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.dish.Dish;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Meal {

    @ToString.Include
    private final MealType type;

    @Setter
    private Dish dish;

    public static Meal from(MealEntity mealEntity) {
        Meal meal = new Meal(mealEntity.getMealType());
        meal.setDish(Dish.from(mealEntity.getDish()));
        return meal;
    }
}
