package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.meal.Meal
import com.sasagui.onmangequoi.meal.MealType

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

    def "slowDishOnWeekDaysScorer - day is weekend: #isWeekendValue and isSlowValue is #isSlowValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            isSlow() >> isSlowValue
        }

        def dayMock = Mock(Day) {
            isWeekend() >> isWeekendValue
        }

        def ctx = new DishScoringContext(dishMock, dayMock, Mock(Meal), [] as Set, [])

        expect:
        config.slowDishOnWeekDaysScorer().score(ctx) == expected

        where:
        isWeekendValue | isSlowValue | expected
        true           | true         | 1
        true           | false        | 0
        false          | true         | -1
        false          | false        | 0
    }

    def "randomScorer - returns int between -1 and 1"() {
        when:
        def score = config.randomScorer().score(Mock(DishScoringContext))

        then:
        score <= 1

        and:
        score >= -1
    }

    def "veganDishForDinnerScorer - dish is vegan: #isVeganValue and meal type is #mealTypeValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            isVegan() >> isVeganValue
        }

        def mealMock = Mock(Meal) {
            getType() >> mealTypeValue
        }

        def ctx = new DishScoringContext(dishMock, Mock(Day), mealMock, [dish1] as Set, [])

        expect:
        config.veganDishForDinnerScorer().score(ctx) == expected

        where:
        isVeganValue | mealTypeValue   | expected
        true         | MealType.LUNCH  | 0
        false        | MealType.LUNCH  | 0
        true         | MealType.DINNER | 1
        false        | MealType.DINNER | 0
    }
}
