package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.dish.DishSelector
import com.sasagui.onmangequoi.dish.DishService

class MealPlanGeneratorSpec extends OnMangeQuoiSpec {

    def dishServiceMock = Mock(DishService)

    def dishSelectorMock = Mock(DishSelector)

    def generator = new MealPlanGenerator(dishServiceMock, dishSelectorMock)

    def "generateMealPlan - week given - returns a new meal plan populated with selected dishes"() {
        when:
        def result = generator.generateMealPlan(weekMock)

        then: "calls dish service to load dishes"
        1 * dishServiceMock.listDishes(null) >> [dish1, dish2]

        and: "first call contains all dishes"
        1 * dishSelectorMock.selectDish([dish1, dish2], _ as Day, _ as Meal, _ as Set, []) >> dish1

        and: "select dish is removed from list in other calls"
        9 * dishSelectorMock.selectDish([dish2], _ as Day, _ as Meal, _ as Set, []) >> dish1

        and:
        result.getDays().every({ it.getMeals().every({ it.getDish() == dish1 })})
    }
}
