package com.sasagui.onmangequoi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Week {

    @ToString.Include
    private final Year year;

    @ToString.Include
    private final int number;

    private final LocalDate start;

    private final LocalDate end;

    private final List<Day> days = new ArrayList<>();

    @Setter
    private String season;

    public Week(final Year year, final int number, final LocalDate start, final LocalDate end) {
        this.year = year;
        this.number = number;
        this.start = start;
        this.end = end;
    }

    @JsonIgnore
    public List<Meal> getMeals() {
        throw new UnsupportedOperationException();
    }
}
