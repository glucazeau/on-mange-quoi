package com.sasagui.onmangequoi

import java.time.LocalDate
import java.time.Year

class WeekServiceSpec extends OnMangeQuoiSpec {

    def manager = new WeekService()

    def "getCurrentWeek - expects current year and a week number between 1 and 53"() {
        when:
        def result = manager.getCurrentWeek()

        then:
        result.year == Year.of(LocalDate.now().year)

        and:
        result.number > 0 && result.number < 53
    }

    def "getWeek - week number is #givenWeekNumber and year is #givenYear - returns week instance with expected values"() {
        when:
        def result = manager.getWeek(Year.of(givenYear), givenWeekNumber)

        then:
        result.year == Year.of(givenYear)

        and:
        result.number == givenWeekNumber

        and:
        result.start == LocalDate.parse(expectedStart)

        and:
        result.end == LocalDate.parse(expectedEnd)

        and:
        result.season == expectedSeason

        and:
        result.nextWeek.year() == Year.of(nextWeekYear)

        and:
        result.nextWeek.number() == nextWeekNumber

        and:
        result.previousWeek.year() == Year.of(previousWeekYear)

        and:
        result.previousWeek.number() == previousWeekNumber

        where:
        givenYear | givenWeekNumber | previousWeekYear | previousWeekNumber | nextWeekYear | nextWeekNumber | expectedStart | expectedEnd  | expectedSeason
        2026      | 1               | 2025             | 52                 | 2026         | 2              | "2025-12-29"  | "2026-01-04" | Season.WINTER
        2026      | 2               | 2026             | 1                  | 2026         | 3              | "2026-01-05"  | "2026-01-11" | Season.WINTER
        2026      | 19              | 2026             | 18                 | 2026         | 20             | "2026-05-04"  | "2026-05-10" | Season.SPRING
        2026      | 32              | 2026             | 31                 | 2026         | 33             | "2026-08-03"  | "2026-08-09" | Season.SUMMER
        2026      | 46              | 2026             | 45                 | 2026         | 47             | "2026-11-09"  | "2026-11-15" | Season.AUTUMN
        2026      | 52              | 2026             | 51                 | 2026         | 53             | "2026-12-21"  | "2026-12-27" | Season.WINTER
        2026      | 53              | 2026             | 52                 | 2027         | 1              | "2026-12-28"  | "2027-01-03" | Season.WINTER
    }

}
