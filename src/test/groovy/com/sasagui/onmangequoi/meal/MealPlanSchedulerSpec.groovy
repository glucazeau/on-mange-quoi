package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.calendar.WeekService

class MealPlanSchedulerSpec extends OnMangeQuoiSpec {

    def weekServiceMock = Mock(WeekService)

    def mealPlanServiceMock = Mock(MealPlanService)

    def mealPlanGeneratorMock = Mock(MealPlanGenerator)

    def scheduler = new MealPlanScheduler(weekServiceMock, mealPlanServiceMock, mealPlanGeneratorMock)

    def "generateNextWeekMealPlan - a meal plan already exists - no further action"() {
        given:
        def nextWeek = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 13
        }

        def mealPlanMock = Mock(MealPlan)

        when:
        scheduler.generateNextWeekMealPlan()

        then: "computes current week"
        1 * weekServiceMock.getCurrentWeek() >> weekMock

        and: "computes next week"
        1 * weekServiceMock.getNextWeek(weekMock) >> nextWeek

        and: "loads meal plan"
        1 * mealPlanServiceMock.getMealPlan(nextWeek) >> Optional.empty()

        and: "generates new meal plan"
        1 * mealPlanGeneratorMock.generateMealPlan(nextWeek) >> mealPlanMock

        and: "saves new meal plan"
        1 * mealPlanServiceMock.saveMealPlan(mealPlanMock)
    }

    def "generateNextWeekMealPlan - no meal plan exists - calls generator and saves result"() {
        given:
        def nextWeek = Mock(Week) {
            getYear() >> 2026
            getNumber() >> 13
        }

        def mealPlanMock = Mock(MealPlan)

        when:
        scheduler.generateNextWeekMealPlan()

        then: "computes current week"
        1 * weekServiceMock.getCurrentWeek() >> weekMock

        and: "computes next week"
        1 * weekServiceMock.getNextWeek(weekMock) >> nextWeek

        and: "loads meal plan"
        1 * mealPlanServiceMock.getMealPlan(nextWeek) >> Optional.of(mealPlanMock)

        and: "does not generate meal plan"
        0 * mealPlanGeneratorMock.generateMealPlan(_)

        and: "does not save meal plan"
        0 * mealPlanServiceMock.saveMealPlan(_)
    }
}
