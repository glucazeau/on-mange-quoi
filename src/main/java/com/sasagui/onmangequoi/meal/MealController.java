package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import java.time.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("meals")
@AllArgsConstructor
public class MealController {

    private final WeekService weekService;

    private final MealService mealService;

    private final MealPlanService mealPlanService;

    @PutMapping(path = "/year/{year}/week/{weekNumber}/day/{day}/meal/{type}")
    public MealPlan setMeal(
            @PathVariable int year,
            @PathVariable int weekNumber,
            @PathVariable DayOfWeek day,
            @PathVariable MealType type,
            @RequestBody long dishId) {
        Week week = weekService.getWeek(year, weekNumber);
        mealService.putMeal(week, day, type, dishId);
        return mealPlanService.getMealPlan(week);
    }
}
