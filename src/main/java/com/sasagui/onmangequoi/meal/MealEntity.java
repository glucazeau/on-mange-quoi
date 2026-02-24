package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.dish.DishEntity;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "meal")
@NoArgsConstructor
public class MealEntity {

    @EmbeddedId
    private MealId id;

    @Setter
    @OneToOne
    @JoinColumn(name = "dish_id")
    private DishEntity dish;

    @Setter
    @ManyToOne
    @JoinColumn(name = "plan_year", referencedColumnName = "plan_year", insertable = false, updatable = false)
    @JoinColumn(name = "week_number", referencedColumnName = "week_number", insertable = false, updatable = false)
    private MealPlanEntity mealPlan;

    public MealEntity(MealId id, DishEntity dishEntity) {
        this.id = id;
        this.dish = dishEntity;
    }

    public DayOfWeek getDayOfWeek() {
        return id.getDayOfWeek();
    }

    public MealType getMealType() {
        return id.getMealType();
    }
}
