package com.sasagui.onmangequoi;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Entity
@Table(name = "meal_plan")
@NoArgsConstructor
public class MealPlanEntity {

    @EmbeddedId
    private MealPlanId id;

    @Setter
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "status")
    private MealPlanStatus status = MealPlanStatus.PENDING_APPROVAL;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL)
    private final List<MealEntity> meals = new ArrayList<>();

    public MealPlanEntity(MealPlanId id) {
        this.id = id;
    }

    public void addMeal(MealEntity meal) {
        meals.add(meal);
        meal.setMealPlan(this);
    }
}
