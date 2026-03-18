package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
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
        MealPlan plan = mealPlanGenerator.generateMealPlan(nextWeek);
        mealPlanService.saveMealPlan(plan);
    }
}
