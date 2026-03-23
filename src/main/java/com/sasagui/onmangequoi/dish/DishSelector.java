package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.meal.Meal;
import jakarta.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class DishSelector {

    private final List<DishScorer> scorers;

    @PostConstruct
    public void logScorers() {
        log.info("{} dish scorers configured}", scorers.size());
    }

    public Dish selectDish(
            List<Dish> dishes,
            DayOfWeek day,
            Meal meal,
            Set<Dish> currentWeekDishes,
            Set<Dish> previousWeekDishes,
            Set<Dish> olderWeeksDishes) {

        if (dishes.isEmpty()) {
            log.error("No dish to score, returning empty dish");
            return Dish.empty();
        } else {
            log.info("Scoring {} dishes for {} of {}", dishes.size(), day, meal);
            List<ScoredDish> scoredDishes = dishes.stream().map(ScoredDish::new).collect(Collectors.toList());
            for (ScoredDish scoredDish : scoredDishes) {
                DishScoringContext context = new DishScoringContext(
                        scoredDish.getDish(),
                        Day.from(day),
                        meal,
                        currentWeekDishes,
                        previousWeekDishes,
                        olderWeeksDishes);
                for (DishScorer scorer : scorers) {
                    scoredDish.adjustScore(scorer.score(context));
                }
            }
            scoredDishes.sort(Collections.reverseOrder());
            ScoredDish highestScoredDish = scoredDishes.getFirst();
            log.info("Dish with highest score is {}", highestScoredDish);
            return highestScoredDish.getDish();
        }
    }
}
