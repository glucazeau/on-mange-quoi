package com.sasagui.onmangequoi.dish;

@FunctionalInterface
public interface DishScorer {

    float score(DishScoringContext context);
}
