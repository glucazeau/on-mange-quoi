package com.sasagui.onmangequoi.dish;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class ScoredDish implements Comparable<ScoredDish> {

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Dish dish;

    @Setter
    @ToString.Include
    private int score;

    @Override
    public int compareTo(@NotNull ScoredDish other) {
        return Integer.compare(this.score, other.score);
    }
}
