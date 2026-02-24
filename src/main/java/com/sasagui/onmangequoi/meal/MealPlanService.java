package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.calendar.WeekService;
import com.sasagui.onmangequoi.dish.DishEntity;
import com.sasagui.onmangequoi.dish.DishRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MealPlanService {

    private final WeekService weekService;

    private final MealPlanRepository mealPlanRepository;

    private final DishRepository dishRepository;

    public MealPlan getOrGenerateMealPlan(int year, int weekNumber) {
        log.info("Searching for a meal plan for week #{} of #{}", weekNumber, year);
        Week week = weekService.getWeek(year, weekNumber);
        MealPlanId mealPlanId = new MealPlanId(week.getYear(), week.getNumber());
        Optional<MealPlanEntity> optionalMealPlanEntity = mealPlanRepository.findById(mealPlanId);
        if (optionalMealPlanEntity.isPresent()) {
            log.info("Meal plan found for {}", week);
            return MealPlan.from(week, optionalMealPlanEntity.get());
        } else {
            log.info("No meal plan found for {}", week);
            return null;
        }
    }

    public MealPlanEntity from(MealPlan mealPlan) {
        Week week = mealPlan.getWeek();
        log.info("Building MealPlan entity for week {}", week);
        MealPlanId mealPlanId = new MealPlanId(week.getYear(), week.getNumber());

        MealPlanEntity mealPlanEntity = new MealPlanEntity(mealPlanId);

        for (Day day : mealPlan.getDays()) {
            for (Meal meal : day.getMeals()) {
                mealPlanEntity.addMeal(buildMealEntity(week, day, meal));
            }
        }
        return mealPlanEntity;
    }

    private MealEntity buildMealEntity(Week week, Day day, Meal meal) {
        MealId mealId = new MealId(week.getYear(), week.getNumber(), day.getDayOfWeek(), meal.getType());

        DishEntity dishEntity = dishRepository.getReferenceById(meal.getDish().getId());
        return new MealEntity(mealId, dishEntity);
    }
}
