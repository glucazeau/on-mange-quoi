package com.sasagui.onmangequoi.meal

import static org.hamcrest.Matchers.everyItem
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sasagui.onmangequoi.MvcSpecification
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

    def "GET /meal-plans/current - no meal plan exists for the current week - compute current week, loads empty meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/current"))

        then: "calls week service to compute current week"
        1 * weekService.getCurrentWeek() >> weekMock

        and: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getMealPlan(weekMock) >> Optional.empty()

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
                .andExpect(jsonPath('\$.days.length()').value(7))
                .andExpect(jsonPath('\$.days[*].meals[*].dish.label', everyItem(is("rien de prévu 🙁"))))
    }

    def "GET /meal-plans/current - a meal plan exists for the current week - compute current week, loads meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/current"))

        then: "calls week service to compute current week"
        1 * weekService.getCurrentWeek() >> weekMock

        and: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getMealPlan(weekMock) >> Optional.of(mealPlan)

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
    }

    def "GET /year/{year}/week/{number} - a meal plan exists for the requested week - computes week, loads meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/year/2026/week/12"))

        then: "calls week service to load week"
        1 * weekService.getWeek(2026, 12) >> weekMock

        and: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getMealPlan(weekMock) >> Optional.of(mealPlan)

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
    }

    def "GET /meal-plans/year/2026/week/12 - no meal plan exists for the requested week - compute week, loads empty meal plan and returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/meal-plans/year/2026/week/12"))

        then: "calls week service to compute current week"
        1 * weekService.getWeek(2026, 12) >> weekMock

        and: "calls meal plan service to search for a meal plan"
        1 * mealPlanService.getMealPlan(weekMock) >> Optional.empty()

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.week.year').value(2026))
                .andExpect(jsonPath('\$.week.number').value(12))
                .andExpect(jsonPath('\$.week.previousWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.previousWeek.number').value(11))
                .andExpect(jsonPath('\$.week.nextWeek.year').value(2026))
                .andExpect(jsonPath('\$.week.nextWeek.number').value(13))
                .andExpect(jsonPath('\$.days.length()').value(7))
                .andExpect(jsonPath('\$.days[*].meals[*].dish.label', everyItem(is("rien de prévu 🙁"))))
    }

}
