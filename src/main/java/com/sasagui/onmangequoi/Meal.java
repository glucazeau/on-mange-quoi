package com.sasagui.onmangequoi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Meal {

    private final MealType type;

    @Setter
    private Dish dish;
}
