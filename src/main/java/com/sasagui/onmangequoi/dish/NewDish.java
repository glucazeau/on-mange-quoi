package com.sasagui.onmangequoi.dish;

import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class NewDish {

    @NotBlank(message = "Dish label must not be blank")
    @ToString.Include
    private String label;

    private boolean slow;

    private boolean quick;

    private boolean fromRestaurant;

    private boolean vegan;

    private boolean fish;

    private boolean kidLunch;

    private Set<Integer> months = new HashSet<>();
}
