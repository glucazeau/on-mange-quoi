package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.dish.Dish;
import com.sasagui.onmangequoi.dish.DishEntity;
import com.sasagui.onmangequoi.dish.DishRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MealPlanService {

    private final MealRepository mealRepository;

    private final DishRepository dishRepository;

    public MealPlan getMealPlan(Week week) {
        log.info("Loading meal plan for week {}", week);
        Specification<MealEntity> spec = MealRepository.fromWeek(week);
        List<MealEntity> meals = mealRepository.findAll(spec);
        return MealPlan.from(week, meals);
    }

    public void saveMealPlan(MealPlan mealPlan) {
        log.info("Saving meal plan for week {}", mealPlan.getWeek());
        List<MealEntity> entities = new ArrayList<>();
        log.debug("Ignoring meals with empty dish");
        List<Meal> meals = mealPlan.getMeals().stream()
                .filter(m -> !Dish.empty().equals(m.getDish()))
                .toList();
        log.info("Persisting {} meals", meals.size());
        for (Meal meal : meals) {
            entities.add(buildMealEntity(mealPlan.getWeek(), meal));
        }
        mealRepository.saveAll(entities);
    }

    private MealEntity buildMealEntity(Week week, Meal meal) {
        MealId mealId = new MealId(week.getYear(), week.getNumber(), meal.getDayOfWeek(), meal.getType());
        DishEntity dishEntity = dishRepository.getReferenceById(meal.getDish().getId());
        return new MealEntity(mealId, dishEntity);
    }
}
