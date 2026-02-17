package com.sasagui.onmangequoi;

import lombok.EqualsAndHashCode;
import lombok.ToString;

public record Dish(Long id, @ToString.Include @EqualsAndHashCode.Include String label, boolean slow, boolean quick,
                   boolean fromRestaurant, boolean vegan) {

}
