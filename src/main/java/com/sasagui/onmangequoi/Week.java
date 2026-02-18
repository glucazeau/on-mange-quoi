package com.sasagui.onmangequoi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
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

    @JsonIgnore
    public List<Meal> getMeals() {
        throw new UnsupportedOperationException();
    }
}
