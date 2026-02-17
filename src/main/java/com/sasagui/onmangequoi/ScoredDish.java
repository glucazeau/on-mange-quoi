package com.sasagui.onmangequoi;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ScoredDish implements Comparable<ScoredDish> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Dish dish;

    @Setter
    @ToString.Include
    private int score;

    public ScoredDish(final Dish dish) {
        this.dish = dish;
        this.score = 0;
    }

    @Override
    public int compareTo(@NotNull ScoredDish other) {
        return Integer.compare(other.score, this.score);
    }
}
