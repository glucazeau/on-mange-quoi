package com.sasagui.onmangequoi.calendar;

import java.time.Month;

public enum Season {
    WINTER,
    SPRING,
    SUMMER,
    AUTUMN;

    public static Season fromMonth(Month month) {
        return switch (month) {
            case DECEMBER, JANUARY, FEBRUARY -> WINTER;
            case MARCH, APRIL, MAY -> SPRING;
            case JUNE, JULY, AUGUST -> SUMMER;
            case SEPTEMBER, OCTOBER, NOVEMBER -> AUTUMN;
        };
    }
}
