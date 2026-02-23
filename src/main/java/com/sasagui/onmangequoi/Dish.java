package com.sasagui.onmangequoi;

import lombok.EqualsAndHashCode;
import lombok.ToString;

public record Dish(
        @ToString.Include @EqualsAndHashCode.Include long id,
        String label,
        boolean slow,
        boolean quick,
        boolean fromRestaurant,
        boolean vegan) {

    public static Dish from(DishEntity dish) {
        return new Dish(
                dish.getId(), dish.getLabel(), dish.isSlow(), dish.isQuick(), dish.isFromRestaurant(), dish.isVegan());
    }
}
