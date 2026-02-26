package com.sasagui.onmangequoi.calendar

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.meal.MealType
import java.time.DayOfWeek
import java.time.LocalDate

class DaySpec extends OnMangeQuoiSpec {

    def "isToday - day date is #dayDate - returns #expectedResult"() {
        given:
        Day day = Day.dinner(DayOfWeek.MONDAY)
        day.setDate(dayDate)

        expect:
        day.isToday() == expectedResult

        where:
        dayDate                    | expectedResult
        LocalDate.of(2023, 12, 31) | false
        LocalDate.now()            | true
    }

    def "dinner - dayOfWeek given - returns a day with one meal with type DINNER and given dayOfWeek"() {
        when:
        def result = Day.dinner(DayOfWeek.MONDAY)

        then: "dayofWeek is the given one"
        result.getDayOfWeek() === DayOfWeek.MONDAY

        and: "day contains one meal"
        result.getMeals().size() == 1

        and: "contained meal is of DINNER type"
        result.getMeals()[0].getType() == MealType.DINNER
    }

    def "lunchAndDinner - dayOfWeek given - returns a day with two meals, one with type LUNCH and the other with type DINNER, and given dayOfWeek"() {
        when:
        def result = Day.lunchAndDinner(DayOfWeek.TUESDAY)

        then: "dayofWeek is the given one"
        result.getDayOfWeek() === DayOfWeek.TUESDAY

        and: "day contains one meal"
        result.getMeals().size() == 2

        and: "first contained meal is of LUNCH type"
        result.getMeals()[0].getType() == MealType.LUNCH

        and: "second contained meal is of DINNER type"
        result.getMeals()[1].getType() == MealType.DINNER
    }
}
