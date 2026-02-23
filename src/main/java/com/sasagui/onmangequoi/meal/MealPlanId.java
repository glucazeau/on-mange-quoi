package com.sasagui.onmangequoi.meal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.Year;
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

    public MealPlanId(Year year, int weekNumber) {
        this.year = year.getValue();
        this.weekNumber = weekNumber;
    }
}
