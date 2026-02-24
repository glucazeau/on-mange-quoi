package com.sasagui.onmangequoi.meal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MealPlanId implements Serializable {

    @Column(name = "plan_year")
    private int year;

    @Column(name = "week_number")
    private int weekNumber;
}
