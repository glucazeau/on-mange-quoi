package com.sasagui.onmangequoi.dish;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Set;
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

    private final boolean fish;

    private final boolean kidLunch;

    private Set<Integer> months;

    public static Dish from(DishEntity dish) {
        return new Dish(
                dish.getId(),
                dish.getLabel(),
                dish.isSlow(),
                dish.isQuick(),
                dish.isFromRestaurant(),
                dish.isVegan(),
                dish.isFish(),
                dish.isKidLunch(),
                dish.getMonths());
    }

    public static Dish empty() {
        return new Dish(
                -1, "rien de prévu \uD83D\uDE41", false, false, false, false, false, false, Collections.emptySet());
    }

    @JsonProperty
    public boolean availableAllYear() {
        return months.size() == 12;
    }
}
