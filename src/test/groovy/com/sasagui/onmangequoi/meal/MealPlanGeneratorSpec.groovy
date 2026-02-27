package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.calendar.WeekService
import com.sasagui.onmangequoi.dish.Dish
import com.sasagui.onmangequoi.dish.DishSelector
import com.sasagui.onmangequoi.dish.DishService

class MealPlanGeneratorSpec extends OnMangeQuoiSpec {

    def weekServiceMock = Mock(WeekService)

    def dishServiceMock = Mock(DishService)

    def mealPlanServiceMock = Mock(MealPlanService)

    def dishSelectorMock = Mock(DishSelector)

    def generator = new MealPlanGenerator(weekServiceMock, dishServiceMock, mealPlanServiceMock, dishSelectorMock)

    def "generateMealPlan - week given - returns a new meal plan populated with selected dishes"() {
        given:
        def previousWeek = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 11
            getPreviousWeek() >> Mock(Week.WeekRef) {
                year() >> 2026
                number() >> 10
            }
        }

        def firstWeekBefore = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 10
            getPreviousWeek() >> Mock(Week.WeekRef) {
                year() >> 2026
                number() >> 9
            }
        }
        def secondWeekBefore = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 9
        }

        def dishMock1 = Mock(Dish)
        def dishMock2 = Mock(Dish)
        def dishMock3 = Mock(Dish)
        def dishMock4 = Mock(Dish)

        def previousWeekMealPlan = Mock(MealPlan) {
            getDishes() >> [dishMock1, dishMock2]
        }

        def mealPlan1 = Mock(MealPlan) {
            getDishes() >> [dishMock3, dishMock4]
        }

        def mealPlan2 = Mock(MealPlan) {
            getDishes() >> [dishMock4, dishMock3]
        }

        when:
        def result = generator.generateMealPlan(weekMock)

        then: "calls dish service to load dishes"
        1 * dishServiceMock.listDishes(null) >> [dish1, dish2]

        and: "compute previous week"
        1 * weekServiceMock.getWeek(2026, 11) >> previousWeek

        and: "loads previous week meal plan"
        1 * mealPlanServiceMock.getMealPlan(previousWeek) >> Optional.of(previousWeekMealPlan)

        and: "compute first week before previous week"
        1 * weekServiceMock.getWeek(2026, 10) >> firstWeekBefore

        and: "loads meal plean from first week before previous week"
        1 * mealPlanServiceMock.getMealPlan(firstWeekBefore) >> Optional.of(mealPlan1)

        and: "compute second week before previous week"
        1 * weekServiceMock.getWeek(2026, 9) >> secondWeekBefore

        and: "loads meal plean from second week before previous week"
        1 * mealPlanServiceMock.getMealPlan(secondWeekBefore) >> Optional.of(mealPlan2)

        and: "first call contains all dishes"
        1 * dishSelectorMock.selectDish([dish1, dish2], _ as Day, _ as Meal, [dishMock1, dishMock2] as Set, [dishMock3, dishMock4] as Set) >> dish1

        and: "select dish is removed from list in other calls"
        9 * dishSelectorMock.selectDish([dish2], _ as Day, _ as Meal, [dishMock1, dishMock2] as Set, [dishMock3, dishMock4] as Set) >> dish1

        and:
        result.getDays().every({ it.getMeals().every({ it.getDish() == dish1 })})
    }
}
