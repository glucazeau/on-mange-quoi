package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.meal.MealType;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DishScorerConfig {

    Random random = new Random();

    @Bean
    public DishScorer quickDishOnWeekDayScorer() {
        return context -> {
            log.debug("Scoring quick dish and week day");
            if (context.getDish().isQuick() && !context.getDay().isWeekend()) {
                return 1;
            }
            return 0;
        };
    }

    @Bean
    public DishScorer slowDishOnWeekDaysScorer() {
        return context -> {
            log.debug("Scoring slow dish and day type");
            if (context.getDish().isSlow()) {
                if (context.getDay().isWeekend()) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        };
    }

    @Bean
    public DishScorer randomScorer() {
        return context -> {
            log.debug("Random scoring");
            return random.nextInt(-1, 1);
        };
    }

    @Bean
    public DishScorer veganDishForDinnerScorer() {
        return context -> {
            log.debug("Scoring vegan dish for dinner");
            if (context.getDish().isVegan()
                    && MealType.DINNER.equals(context.getMeal().getType())) {
                return 1;
            }
            return 0;
        };
    }

    @Bean
    public DishScorer dishUsedLastWeekScorer() {
        return context -> {
            log.debug("Scoring dish used last week");
            if (context.getLastWeekDishes().contains(context.getDish())) {
                return -2;
            }
            return 0;
        };
    }

    @Bean
    public DishScorer dishUsedOlderWeeksScorer() {
        return context -> {
            log.debug("Scoring dish used previous weeks");
            if (context.getPreviousWeeksDishes().contains(context.getDish())) {
                return -1;
            }
            return 0;
        };
    }

    public DishScorer dishNotUsedLastWeekAndPreviousWeeks() {
        return context -> {
            log.debug("Scoring dish not used last week and previous weeks");
            if (!context.getLastWeekDishes().contains(context.getDish())
                    && !context.getPreviousWeeksDishes().contains(context.getDish())) {
                return 1;
            }
            return 0;
        };
    }
}
