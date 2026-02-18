package com.sasagui.onmangequoi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Day {

    private final DayOfWeek dayOfWeek;

    private final LocalDate date;

    private final List<Meal> meals;
}
