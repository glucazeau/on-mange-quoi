package com.sasagui.onmangequoi

import static java.time.DayOfWeek.*

import java.time.Year

class MealPlanSpec extends OnMangeQuoiSpec {

    def "schoolWeek - week given - returns a meal plan with given week, expected days and meals"() {
        given:
        def weekMock = Mock(Week) {
            getYear() >> Year.of(2026)
            getNumber() >> 2
        }

        when:
        def result = MealPlan.schoolWeek(weekMock)

        then: "meal plan contains 7 days"
        result.getDays().size() == 7

        then: "meal plan contains expected days"
        result.getDays()[0].getDayOfWeek() == MONDAY
        result.getDays()[1].getDayOfWeek() == TUESDAY
        result.getDays()[2].getDayOfWeek() == WEDNESDAY
        result.getDays()[3].getDayOfWeek() == THURSDAY
        result.getDays()[4].getDayOfWeek() == FRIDAY
        result.getDays()[5].getDayOfWeek() == SATURDAY
        result.getDays()[6].getDayOfWeek() == SUNDAY

        and: "monday, tuesday, thursday and friday contain dinner only"
        def days1 = result.getDays().findAll({ it.getDayOfWeek() in [MONDAY, TUESDAY, THURSDAY, FRIDAY] })
        days1.every({ it -> it.getMeals().size() == 1 })
        days1.every({ it -> it.getMeals()[0].getType() == MealType.DINNER })

        and: "wednesday, saturday and sunday contain lunch and dinner"
        def days2 = result.getDays().findAll({ it.getDayOfWeek() in [WEDNESDAY, SATURDAY, SUNDAY] })
        days2.every({ it -> it.getMeals().size() == 2 })
        days2.every({ it -> it.getMeals()[0].getType() == MealType.LUNCH })
        days2.every({ it -> it.getMeals()[1].getType() == MealType.DINNER })
    }
}
