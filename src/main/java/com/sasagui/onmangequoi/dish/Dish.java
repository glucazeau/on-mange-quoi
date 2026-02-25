package com.sasagui.onmangequoi.dish;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dish {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final long id;

    private final String label;

    private final boolean slow;

    private final boolean quick;

    private final boolean fromRestaurant;

    private final boolean vegan;

    public static Dish from(DishEntity dish) {
        return new Dish(
                dish.getId(), dish.getLabel(), dish.isSlow(), dish.isQuick(), dish.isFromRestaurant(), dish.isVegan());
    }

    public static Dish empty() {
        return new Dish(-1, "rien de prévu \uD83D\uDE41", false, false, false, false);
    }
}
