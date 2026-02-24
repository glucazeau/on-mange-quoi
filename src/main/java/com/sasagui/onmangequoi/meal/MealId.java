package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.calendar.DayOfWeekConverter;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MealId {

    @Column(name = "plan_year")
    private int year;

    @Column(name = "week_number")
    private int weekNumber;

    @Convert(converter = DayOfWeekConverter.class)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;
}
