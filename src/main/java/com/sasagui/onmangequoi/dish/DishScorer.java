package com.sasagui.onmangequoi.dish;

@FunctionalInterface
public interface DishScorer {

    int score(DishScoringContext context);
}
