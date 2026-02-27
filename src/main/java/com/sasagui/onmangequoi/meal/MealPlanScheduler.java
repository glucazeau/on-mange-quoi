package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MealPlanScheduler {

    private final WeekService weekService;

    private final MealPlanService mealPlanService;

    private final MealPlanGenerator mealPlanGenerator;

    public void generateNextWeekMealPlan() {
        Week currentWeek = weekService.getCurrentWeek();
        Week nextWeek = weekService.getNextWeek(currentWeek);

        log.info("Generating meal plan for next week {}", nextWeek);
        Optional<MealPlan> mealPlan = mealPlanService.getMealPlan(nextWeek);
        if (mealPlan.isPresent()) {
            log.info("A meal plan already exists for next week {}", currentWeek);
        } else {
            MealPlan newMealPlan = mealPlanGenerator.generateMealPlan(nextWeek);
            mealPlanService.saveMealPlan(newMealPlan);
        }
    }
}
