package com.sasagui.onmangequoi.calendar

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.meal.MealType
import java.time.DayOfWeek
import java.time.LocalDate

class DaySpec extends OnMangeQuoiSpec {

    def "isToday - day date is #dayDate - returns #expectedResult"() {
        given:
        Day day = Day.from(DayOfWeek.MONDAY, [])
        day.setDate(dayDate)

        expect:
        day.isToday() == expectedResult

        where:
        dayDate                    | expectedResult
        LocalDate.of(2023, 12, 31) | false
        LocalDate.now()            | true
    }

    def "isWeekend - day of week is #dayOfWeek - returns #expectedResult"() {
        given:
        Day day = Day.from(dayOfWeek, [])

        expect:
        day.isWeekend() == expectedResult

        where:
        dayOfWeek           | expectedResult
        DayOfWeek.MONDAY    | false
        DayOfWeek.TUESDAY   | false
        DayOfWeek.WEDNESDAY | false
        DayOfWeek.THURSDAY  | false
        DayOfWeek.FRIDAY    | false
        DayOfWeek.SATURDAY  | true
        DayOfWeek.SUNDAY    | true
    }
}
