package com.sasagui.onmangequoi.calendar

import static java.time.DayOfWeek.*

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import java.time.LocalDate
import spock.lang.Shared

class DaySpec extends OnMangeQuoiSpec {

    @Shared
    WeekService weekService = new WeekService()

    def "getDate - day is given a week and a day of week - returns computed date"() {
        expect:
        Day.from(weekMock, dayOfWeek).getDate() == expectedDate

        where:
        dayOfWeek | expectedDate
        MONDAY    | LocalDate.of(2026, 3, 16)
        TUESDAY   | LocalDate.of(2026, 3, 17)
        WEDNESDAY | LocalDate.of(2026, 3, 18)
        THURSDAY  | LocalDate.of(2026, 3, 19)
        FRIDAY    | LocalDate.of(2026, 3, 20)
        SATURDAY  | LocalDate.of(2026, 3, 21)
        SUNDAY    | LocalDate.of(2026, 3, 22)

    }

    def "isToday - #testLabel - returns #expectedResult"() {
        given:
        def week = weekService.getWeek(weekYear, weekNumber)
        Day day = Day.from(week, dayOfWeek)

        expect:
        day.isToday() == expectedResult

        where:
        weekYear                 | weekNumber                          | dayOfWeek                 | testLabel                      | expectedResult
        2023                     | 1                                   | MONDAY                    | "day is Monday of past week"   | false
        LocalDate.now().year     | weekService.getCurrentWeek().number | LocalDate.now().dayOfWeek | "day is today"                 | true
        LocalDate.now().year + 1 | 1                                   | MONDAY                    | "day is Monday of future week" | false
    }

    def "isPast - #testLabel - returns #expectedResult"() {
        given:
        def week = weekService.getWeek(weekYear, weekNumber)
        Day day = Day.from(week, dayOfWeek)

        expect:
        day.isPast() == expectedResult

        where:
        weekYear                 | weekNumber                          | dayOfWeek                 | testLabel                      | expectedResult
        2023                     | 1                                   | MONDAY                    | "day is Monday of past week"   | true
        LocalDate.now().year     | weekService.getCurrentWeek().number | LocalDate.now().dayOfWeek | "day is today"                 | false
        LocalDate.now().year + 1 | 1                                   | MONDAY                    | "day is Monday of future week" | false
    }

    def "isWeekend - day of week is #dayOfWeek - returns #expectedResult"() {
        given:
        Day day = Day.from(dayOfWeek)

        expect:
        day.isWeekend() == expectedResult

        where:
        dayOfWeek           | expectedResult
        MONDAY              | false
        TUESDAY             | false
        WEDNESDAY           | false
        THURSDAY            | false
        FRIDAY              | false
        SATURDAY            | true
        SUNDAY              | true
    }
}
