package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MealPlanScheduler {

    private final WeekService weekService;

    private final MealPlanService mealPlanService;

    private final MealPlanGenerator mealPlanGenerator;

    private ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0 0 6 * * MON")
    public void generateNextWeekMealPlan() {
        Week currentWeek = weekService.getCurrentWeek();
        Week nextWeek = weekService.getNextWeek(currentWeek);

        log.info("Generating meal plan for next week {}", nextWeek);
        MealPlan plan = mealPlanGenerator.generateMealPlan(nextWeek);
        log.info("Saving meal plan for next week");
        mealPlanService.saveMealPlan(plan);
        log.info("Publishing new meal plan event");
        applicationEventPublisher.publishEvent(new NewMealPlanEvent(plan));
    }
}
