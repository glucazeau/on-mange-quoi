package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.meal.Meal

class DishScorerConfigSpec extends OnMangeQuoiSpec {

    def config = new DishScorerConfig()

    def "quickDishOnWeekDayScorer - day is weekend: #isWeekendValue and isQuickValue is #isQuickValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            isQuick() >> isQuickValue
        }

        def dayMock = Mock(Day) {
            isWeekend() >> isWeekendValue
        }

        def ctx = new DishScoringContext(dishMock, dayMock, Mock(Meal), [] as Set, [])

        expect:
        config.quickDishOnWeekDayScorer().score(ctx) == expected

        where:
        isWeekendValue | isQuickValue | expected
        true           | true         | 0
        true           | false        | 0
        false          | true         | 1
        false          | false        | 0
    }
}
