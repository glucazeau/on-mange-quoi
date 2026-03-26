package com.sasagui.onmangequoi.dish;

import com.sasagui.onmangequoi.meal.MealType;
import java.time.DayOfWeek;
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
            if ((context.getDish().isVegan() || context.getDish().isFish())
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

    @Bean
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

    @Bean
    public DishScorer kidLunchAndWednesdayLunch() {
        return context -> {
            log.debug("Scoring kid lunch for Wednesday lunch");
            if (context.getDish().isKidLunch()
                    && DayOfWeek.WEDNESDAY.equals(context.getDay().getDayOfWeek())
                    && MealType.LUNCH.equals(context.getMeal().getType())) {
                return 2;
            }
            return 0;
        };
    }

    @Bean
    public DishScorer soupOnSundayDiner() {
        return context -> {
            String label = context.getDish().getLabel().toLowerCase();
            boolean isSoup = label.contains("soupe") || label.contains("potage") || label.contains("velouté");
            log.debug("Scoring soup for Sunday diner");
            if (isSoup
                    && DayOfWeek.SUNDAY.equals(context.getDay().getDayOfWeek())
                    && MealType.DINNER.equals(context.getMeal().getType())) {
                return 1;
            }
            return 0;
        };
    }

    @Bean
    public DishScorer restaurantDishWithAnotherRestaurantThisWeek() {
        return context -> {
            log.debug("Scoring restaurant dish with restaurant dishes this week");
            return context.getDish().isFromRestaurant() && context.countCurrentWeekRestaurants() > 0 ? -2 : 0;
        };
    }

    @Bean
    public DishScorer restaurantDishWithAnotherRestaurantLastWeek() {
        return context -> {
            log.debug("Scoring restaurant dish with restaurant dishes last week");
            return context.getDish().isFromRestaurant() && context.countLastWeekRestaurants() > 0 ? -1 : 0;
        };
    }
}
