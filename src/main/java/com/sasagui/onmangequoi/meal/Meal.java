package com.sasagui.onmangequoi.meal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sasagui.onmangequoi.dish.Dish;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Meal implements Comparable<Meal> {

    @JsonIgnore
    @EqualsAndHashCode.Include
    private final DayOfWeek dayOfWeek;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final MealType type;

    @Setter
    private Dish dish;

    public static Meal from(MealEntity mealEntity) {
        return new Meal(mealEntity.getDayOfWeek(), mealEntity.getMealType(), Dish.from(mealEntity.getDish()));
    }

    public static Meal empty(DayOfWeek day, MealType type) {
        return new Meal(day, type, Dish.empty());
    }

    @Override
    public int compareTo(@NotNull Meal o) {
        int dayComparison = this.dayOfWeek.compareTo(o.getDayOfWeek());
        if (dayComparison != 0) {
            return dayComparison;
        }
        return this.type.compareTo(o.getType());
    }
}
