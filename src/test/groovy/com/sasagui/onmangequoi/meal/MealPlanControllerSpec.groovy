package com.sasagui.onmangequoi.meal

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sasagui.onmangequoi.MvcSpecification
import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.calendar.WeekService
import org.spockframework.spring.SpringBean
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared

@ContextConfiguration(classes = [MealPlanController])
class MealPlanControllerSpec extends MvcSpecification {

    @SpringBean
    WeekService weekService = Mock(WeekService)

    @SpringBean
    MealPlanService mealPlanService = Mock(MealPlanService)

    @Shared
    def mealPlan

    def setupSpec() {
        mealPlan = MealPlan.schoolWeek(weekMock)
        mealPlan.getDays()[0].getMeals()[0].setDish(dish1)
    }

    def "GET /meal-plans/current - compute current week, loads meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/current"))

        then: "calls week service to retrieve current week"
        1 *  weekService.getCurrentWeek() >> weekMock

        and: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getOrGenerateMealPlan(2026, 12) >> mealPlan

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
    }

    def "GET /year/{year}/week/{number} - loads meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/year/2026/week/12"))

        then: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getOrGenerateMealPlan(2026, 12) >> mealPlan

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
    }
}
