package com.sasagui.onmangequoi.meal

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sasagui.onmangequoi.IntegrationSpec
import java.time.LocalDateTime
import java.time.temporal.WeekFields
import org.springframework.http.MediaType

class MealIntegrationSpec extends IntegrationSpec {

    def "PUT /meals/year/{year}/week/{weekNumber}/day/{day}/meal/{meal} - no meal plan exists - returns HTTP 403"() {
        when:
        def response = mvc.perform(put("/meals/year/2025/week/1/day/MONDAY/meal/DINNER")
                .contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(status().isForbidden())
    }

    def "PUT /meals/year/{year}/week/{weekNumber}/day/{day}/meal/{meal} - meal plan exists but is in the past - returns HTTP 403"() {
        when:
        def response = mvc.perform(put("/meals/year/2026/week/1/day/MONDAY/meal/DINNER")
                .contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(status().isForbidden())
    }

    def "PUT /meals/year/{year}/week/{weekNumber}/day/{day}/meal/{meal} - meal plan exists and is editable - returns HTTP 200 and updated meal plan"() {
        given:
        def now = LocalDateTime.now()
        def year = now.year
        def week = now.get(WeekFields.of(Locale.FRANCE).weekOfYear())

        when:
        def response = mvc.perform(put("/meals/year/${year}/week/${week}/day/MONDAY/meal/DINNER")
                .contentType(MediaType.APPLICATION_JSON).content("12"))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.days[?(@.dayOfWeek == \'MONDAY\')].meals[?(@.type == \'DINNER\')].dish.id').value(12))
    }

}
