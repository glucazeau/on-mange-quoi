package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.Day;
import com.sasagui.onmangequoi.calendar.Week;
import com.sasagui.onmangequoi.dish.DishEntity;
import com.sasagui.onmangequoi.dish.DishRepository;
import java.time.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    private final DishRepository dishRepository;

    public void putMeal(Week week, DayOfWeek day, MealType type, long dishId) {
        log.info("Updating {} on {} for {}", type, day, week);

        if (Day.from(week, day).isPast()) {
            String errorMsg = String.format("Meal for week %s, %s and type %s is in the past", week, day, type);
            log.error(errorMsg);
            throw new MealNotEditableException(errorMsg);
        } else {
            DishEntity dish = dishRepository.getReferenceById(dishId);
            MealId mealId = new MealId(week.getYear(), week.getNumber(), day, type);
            MealEntity mealEntity = mealRepository.findById(mealId).orElse(new MealEntity(mealId, dish));
            log.info("Updating meal with dish #{}", dishId);
            mealEntity.setDish(dish);
            mealRepository.saveAndFlush(mealEntity);
        }
    }
}
