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

    @ToString.Include
    private int score = 0;

    public void adjustScore(int score) {
        this.score = this.score + score;
    }

    @Override
    public int compareTo(@NotNull ScoredDish other) {
        return Integer.compare(this.score, other.score);
    }
}
