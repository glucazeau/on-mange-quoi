package com.sasagui.onmangequoi.meal

import static java.time.DayOfWeek.*

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.calendar.WeekService
import com.sasagui.onmangequoi.dish.Dish
import com.sasagui.onmangequoi.dish.DishSearchCriteria
import com.sasagui.onmangequoi.dish.DishSelector
import com.sasagui.onmangequoi.dish.DishService
import java.time.DayOfWeek
import java.time.LocalDateTime

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
        def thirdWeekBefore = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 8
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

        def mealPlan3 = Mock(MealPlan) {
            getDishes() >> []
        }

        def nextWeekMealPlan = MealPlan.from(weekMock, [mealEntity3])

        when:
        def result = generator.generateMealPlan(weekMock)

        then: "calls dish service to load dishes available in current month"
        1 * dishServiceMock.listDishes(_ as DishSearchCriteria) >> { DishSearchCriteria c ->
            assert c.getMonth() == LocalDateTime.now().getMonth().getValue()
            return [dish1, dish2, dish3]
        }

        and: "check already planned meal for next week to remove dishes used"
        1 * mealPlanServiceMock.getMealPlan(weekMock) >> nextWeekMealPlan

        and: "compute previous week"
        1 * weekServiceMock.getPreviousWeek(weekMock) >> previousWeek

        and: "loads previous week meal plan"
        1 * mealPlanServiceMock.getMealPlan(previousWeek) >> previousWeekMealPlan

        and: "compute three weeks before previous week"
        1 * weekServiceMock.getPreviousWeeks(previousWeek, 3) >> [firstWeekBefore, secondWeekBefore, thirdWeekBefore]

        and: "loads meal plean from first week before previous week"
        1 * mealPlanServiceMock.getMealPlan(firstWeekBefore) >> mealPlan1

        and: "loads meal plean from second week before previous week"
        1 * mealPlanServiceMock.getMealPlan(secondWeekBefore) >> mealPlan2

        and: "loads meal plean from second week before previous week"
        1 * mealPlanServiceMock.getMealPlan(thirdWeekBefore) >> mealPlan3

        and: "first call contains all dishes minus the one already planned"
        1 * dishSelectorMock.selectDish([dish1, dish2], _ as DayOfWeek, _ as Meal, [dish3] as Set, [dishMock1, dishMock2] as Set, [dishMock3, dishMock4] as Set) >> { List<Dish> dishes, DayOfWeek day, Meal meal, Set<Dish> d0, Set<Dish> d1, Set<Dish> d2 ->
            assert day != MONDAY
            return dish1
        }

        and: "selected dish is removed from list in other calls"
        8 * dishSelectorMock.selectDish([dish2], _ as DayOfWeek, _ as Meal, [dish3, dish1] as Set, [dishMock1, dishMock2] as Set, [dishMock3, dishMock4] as Set) >> { List<Dish> dishes, DayOfWeek day, Meal meal, Set<Dish> d0, Set<Dish> d1, Set<Dish> d2 ->
            assert day != MONDAY
            return dish1
        }

        and: "every day has dish1 except Monday which was already planned with dsh3"
        result.getDays().every({ it.getMeals().every({ (it.getDayOfWeek() != MONDAY && it.getDish() == dish1) || it.getDish() == dish3 })})
    }
}
