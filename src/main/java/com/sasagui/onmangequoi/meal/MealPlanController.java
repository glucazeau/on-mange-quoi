package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("meal-plans")
@AllArgsConstructor
public class MealPlanController {

    private final WeekService weekService;

    private final MealPlanService mealPlanService;

    @GetMapping(path = "/current")
    public MealPlan getCurrentMealPlan() {
        log.info("Request received for current week meal plan");
        Week currentWeek = weekService.getCurrentWeek();
        return mealPlanService.getOrGenerateMealPlan(currentWeek.getYear(), currentWeek.getNumber());
    }

    @GetMapping(path = "/year/{year}/week/{weekNumber}")
    public MealPlan getMealPlan(@PathVariable int year, @PathVariable int weekNumber) {
        log.info("Request received for meal plan of week #{} of {}", weekNumber, year);
        return mealPlanService.getOrGenerateMealPlan(year, weekNumber);
    }
}
