package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.meal.Meal
import com.sasagui.onmangequoi.meal.MealType
import java.time.DayOfWeek
import org.junit.jupiter.api.MediaType

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

        def ctx = new DishScoringContext(dishMock, dayMock, Mock(Meal), [] as Set, [] as Set, [] as Set)

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

        def ctx = new DishScoringContext(dishMock, dayMock, Mock(Meal), [] as Set, [] as Set, [] as Set)

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

    def "veganDishForDinnerScorer - dish is vegan: #isVeganValue and fish: #isFishValue and meal type is #mealTypeValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            isVegan() >> isVeganValue
            isFish() >> isFishValue
        }

        def mealMock = Mock(Meal) {
            getType() >> mealTypeValue
        }

        def ctx = new DishScoringContext(dishMock, Mock(Day), mealMock,  [] as Set, [dish1] as Set, [] as Set)

        expect:
        config.veganDishForDinnerScorer().score(ctx) == expected

        where:
        isVeganValue | isFishValue | mealTypeValue   | expected
        true         | true        | MealType.LUNCH  | 0
        false        | false       | MealType.LUNCH  | 0
        false        | true        | MealType.LUNCH  | 0
        true         | false       | MealType.LUNCH  | 0
        true         | true        | MealType.DINNER | 1
        false        | false       | MealType.DINNER | 0
        false        | true        | MealType.DINNER | 1
        true         | false       | MealType.DINNER | 1
    }

    def "dishUsedLastWeekScorer - dish was #testLabel last week - returns #expected"() {
        given:
        def ctx = new DishScoringContext(dishValue, Mock(Day), Mock(Meal), [] as Set, [dish1] as Set, [] as Set)

        expect:
        config.dishUsedLastWeekScorer().score(ctx) == expected

        where:
        dishValue | expected | testLabel
        dish1     | -2       | "used"
        dish2     | 0        | "not used"
    }

    def "dishUsedOlderWeeksScorer - dish was #testLabel previous weeks - returns #expected"() {
        given:
        def ctx = new DishScoringContext(dishValue, Mock(Day), Mock(Meal), [] as Set, [] as Set, [dish1] as Set)

        expect:
        config.dishUsedOlderWeeksScorer().score(ctx) == expected

        where:
        dishValue | expected | testLabel
        dish1     | -1       | "used"
        dish2     | 0        | "not used"
    }

    def "dishNotUsedLastWeekAndPreviousWeeks - dish was #testLabel last week or previous weeks - returns #expected"() {
        given:
        def ctx = new DishScoringContext(dishValue, Mock(Day), Mock(Meal), [] as Set, [dish1] as Set, [dish1] as Set)

        expect:
        config.dishNotUsedLastWeekAndPreviousWeeks().score(ctx) == expected

        where:
        dishValue | expected | testLabel
        dish1     | 0        | "used"
        dish2     | 1        | "not used"
    }

    def "kidLunchAndWednesdayLunch - dish is kid lunch: #kidLunchValue and day is #dayValue and meal is #mealValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            isKidLunch() >> kidLunchValue
        }
        def dayMock = Mock(Day) {
            getDayOfWeek() >> dayValue
        }
        def mealMock = Mock(Meal) {
            getType() >> mealValue
        }
        def ctx = new DishScoringContext(dishMock, dayMock, mealMock, [] as Set, [] as Set, [] as Set)

        expect:
        config.kidLunchAndWednesdayLunch().score(ctx) == expected

        where:
        [kidLunchValue, dayValue, mealValue] << [
                [true, false],
                DayOfWeek.values(),
                MealType.values()
        ].combinations()
        expected = kidLunchValue && dayValue == DayOfWeek.WEDNESDAY && mealValue == MealType.LUNCH ? 2 : 0
    }

    def "soupOnSundayDiner - dish label is: #dishLabelValue and day is #dayValue and meal is #mealValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            getLabel() >> dishLabelValue
        }
        def dayMock = Mock(Day) {
            getDayOfWeek() >> dayValue
        }
        def mealMock = Mock(Meal) {
            getType() >> mealValue
        }
        def ctx = new DishScoringContext(dishMock, dayMock, mealMock, [] as Set, [] as Set, [] as Set)

        expect:
        config.soupOnSundayDiner().score(ctx) == expected

        where:
        [dishLabelValue, dayValue, mealValue] << [
                ["Soupe", "soupe", "Potage", "potage", "Velouté", "velouté", "Poisson pané"],
                DayOfWeek.values(),
                MealType.values()
        ].combinations()
        expected = dishLabelValue != "Poisson pané" && dayValue == DayOfWeek.SUNDAY && mealValue == MealType.DINNER ? 1 : 0
    }

    def "restaurantDishWithAnotherRestaurantThisWeek - #testLabel a dish from a restaurant is in current week - returns #expected"() {
        given:
        def currentWeekDishMock = Mock(Dish) {
            isFromRestaurant() >> currentWeekDishFromRestaurant
        }
        def scoredDishMock = Mock(Dish) {
            isFromRestaurant() >> scoredDishFromRestaurant
        }
        def ctx = new DishScoringContext(scoredDishMock, Mock(Day), Mock(Meal), [currentWeekDishMock] as Set, [] as Set, [] as Set)

        expect:
        config.restaurantDishWithAnotherRestaurantThisWeek().score(ctx) == expected

        where:
        scoredDishFromRestaurant | currentWeekDishFromRestaurant | testLabel                                                                  | expected
        true                     | true                          | "dish is from restaurant and one dish from restaurant is in this week"     | -2
        true                     | false                         | "dish is from restaurant and no dish from restaurant is in this week"      | 0
        false                    | true                          | "dish is not from restaurant and one dish from restaurant is in this week" | 0
        false                    | false                         | "dish is not from restaurant and no dish from restaurant is in this week"  | 0
    }

    def "restaurantDishWithAnotherRestaurantLastWeek - #testLabel a dish from a restaurant is in last week - returns #expected"() {
        given:
        def lastWeekDishMock = Mock(Dish) {
            isFromRestaurant() >> lastWeekDishFromRestaurant
        }
        def scoredDishMock = Mock(Dish) {
            isFromRestaurant() >> scoredDishFromRestaurant
        }
        def ctx = new DishScoringContext(scoredDishMock, Mock(Day), Mock(Meal), [] as Set, [lastWeekDishMock] as Set, [] as Set)

        expect:
        config.restaurantDishWithAnotherRestaurantLastWeek().score(ctx) == expected

        where:
        scoredDishFromRestaurant | lastWeekDishFromRestaurant | testLabel                                                                   | expected
        true                     | true                       | "dish is from restaurant and one dish from restaurant was in last week"     | -1
        true                     | false                      | "dish is from restaurant and no dish from restaurant was in last week"      | 0
        false                    | true                       | "dish is not from restaurant and one dish from restaurant was in last week" | 0
        false                    | false                      | "dish is not from restaurant and no dish from restaurant was in last week"  | 0
    }

    def "tartForLunch -dish label contains 'tarte' and meal type is #mealTypeValue - returns #expected"() {
        given:
        def dishMock = Mock(Dish) {
            getLabel() >> dishLabel
        }
        def mealMock = Mock(Meal) {
            getType() >> mealTypeValue
        }
        def ctx = new DishScoringContext(dishMock, Mock(Day), mealMock, [] as Set, [] as Set, [] as Set)

        expect:
        config.tartForLunch().score(ctx) == expected

        where:
        mealTypeValue   | dishLabel             | expected
        MealType.LUNCH  | "Tarte aux poireaux"  | -1
        MealType.LUNCH  | "tarte aux poireaux"  | -1
        MealType.LUNCH  | "Poulet frites"       | 0
        MealType.DINNER | "Tarte aux poireaux"  | 0
        MealType.DINNER | "tarte aux poireaux"  | 0
    }
}
