package com.sasagui.onmangequoi.dish;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class NewDish {

    @ToString.Include
    private String label;

    private boolean slow;

    private boolean quick;

    private boolean fromRestaurant;

    private boolean vegan;

    private boolean fish;
}
