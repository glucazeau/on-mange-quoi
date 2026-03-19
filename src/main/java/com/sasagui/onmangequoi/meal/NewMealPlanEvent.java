package com.sasagui.onmangequoi.meal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewMealPlanEvent {

    private final MealPlan mealPlan;
}
