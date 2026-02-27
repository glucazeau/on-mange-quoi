package com.sasagui.onmangequoi.calendar;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeekService {

    public Week getCurrentWeek() {
        log.info("Computing current week");
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;

        int weekNumber = today.get(weekFields.weekOfWeekBasedYear());
        int weekYear = today.get(weekFields.weekBasedYear());

        return getWeek(weekYear, weekNumber);
    }

    public Week getNextWeek(Week week) {
        log.info("Computing next week from {}", week);
        return getWeek(week.getNextWeek().year(), week.getNextWeek().number());
    }

    public Week getPreviousWeek(Week week) {
        log.info("Computing previous week from {}", week);
        return getWeek(week.getPreviousWeek().year(), week.getPreviousWeek().number());
    }

    public Week getWeek(int year, int weekNumber) {
        log.info("Computing week #{} of {}", weekNumber, year);
        return Week.builder().year(year).number(weekNumber).build();
    }
}
