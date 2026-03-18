package com.sasagui.onmangequoi

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import java.time.LocalDateTime
import java.time.temporal.WeekFields
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.postgresql.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

@ActiveProfiles("integration-test")
@SpringBootTest
@AutoConfigureMockMvc
@Stepwise
class ApiIntegrationSpec extends Specification {

    static PostgreSQLContainer postgresql = new PostgreSQLContainer("postgres:16.0")
            .withDatabaseName("brand-portal")
            .withUsername("brand-portal")
            .withPassword("brand-portal")

    @Shared
    PostgreSQLContainer postgresqlContainer = postgresql

    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl)
        registry.add("spring.datasource.username", postgresql::getUsername)
        registry.add("spring.datasource.password", postgresql::getPassword)
    }

    @Autowired
    protected MockMvc mvc

    def setupSpec() {
        postgresqlContainer.start()
    }

    def cleanupSpec() {
        postgresqlContainer.stop()
    }

    def "GET /dishes - no request body sent - returns HTTP 200 and dishes JSON results"() {
        when:
        def response = mvc.perform(get("/dishes")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$", hasSize(29)))
    }

    def "POST /dishes - new dish body sent - returns HTTP 201"() {
        when:
        def response = mvc.perform(post("/dishes")
                .contentType(MediaType.APPLICATION_JSON).content("""{
                            "label": "Poulet frites",
                            "slow": true,
                            "quick": false,
                            "fromRestaurant": false,
                            "vegan": false,
                            "fish": true,
                            "kidLunch": false,
                            "months": [
                                4, 5, 6
                            ]
                        }"""))

        then:
        response.andExpect(status().isCreated())
    }

    def "GET /dishes/{dishId} - new dish has been added"() {
        when:
        def response = mvc.perform(get("/dishes/30")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.id').value(30))
                .andExpect(jsonPath('\$.label').value("Poulet frites"))
                .andExpect(jsonPath('\$.slow').value(true))
                .andExpect(jsonPath('\$.quick').value(false))
                .andExpect(jsonPath('\$.fromRestaurant').value(false))
                .andExpect(jsonPath('\$.vegan').value(false))
                .andExpect(jsonPath('\$.fish').value(true))
                .andExpect(jsonPath('\$.kidLunch').value(false))
                .andExpect(jsonPath("\$.availableAllYear").value(false))
                .andExpect(jsonPath("\$.months", hasSize(3)))
                .andExpect(jsonPath('\$.months[0]').value(4))
                .andExpect(jsonPath('\$.months[1]').value(5))
                .andExpect(jsonPath('\$.months[2]').value(6))
    }

    def "GET /dishes - new dish has been added"() {
        when:
        def response = mvc.perform(get("/dishes")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$", hasSize(30)))
    }

    def "POST /dishes - new dish body sent with same label - returns HTTP 409"() {
        when:
        def response = mvc.perform(post("/dishes")
                .contentType(MediaType.APPLICATION_JSON).content("""{
                            "label": "Poulet frites",
                            "slow": true,
                            "quick": false,
                            "fromRestaurant": false,
                            "vegan": false
                        }"""))

        then:
        response.andExpect(status().isConflict())
                .andExpect(jsonPath('\$.errors[0].errorMessage').value("A dish with label 'Poulet frites' already exists"))
    }

    def "POST /dishes - new dish body sent with missing label - returns HTTP 400"() {
        when:
        def response = mvc.perform(post("/dishes")
                .contentType(MediaType.APPLICATION_JSON).content("""{
                            "slow": true,
                            "quick": false,
                            "fromRestaurant": false,
                            "vegan": false
                        }"""))

        then:
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.errors[0].errorMessage').value("Dish label must not be blank"))
    }

    def "GET /dishes/{dishId} - dish exists - returns HTTP 200 and JSON results"() {
        when:
        def response = mvc.perform(get("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.id').value(1))
                .andExpect(jsonPath('\$.label').value("Sushis"))
                .andExpect(jsonPath('\$.slow').value(false))
                .andExpect(jsonPath('\$.quick').value(false))
                .andExpect(jsonPath('\$.fromRestaurant').value(true))
                .andExpect(jsonPath('\$.vegan').value(false))
                .andExpect(jsonPath('\$.fish').value(false))
                .andExpect(jsonPath('\$.kidLunch').value(false))
    }

    def "PUT /dishes/{dishId} - dish exists - updates dish returns HTTP 204"() {
        when:
        def response = mvc.perform(put("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                    "label": "Sushis (updated)",
                    "slow": true,
                    "quick": true,
                    "fromRestaurant": false,
                    "vegan": true,
                    "fish": true,
                    "kidLunch": true,
                    "months": [1, 2, 3]
                }"""))

        then:
        response.andExpect(status().isOk())
    }

    def "GET /dishes/{dishId} - dish exists and has been updated - returns HTTP 200 and updated JSON results"() {
        when:
        def response = mvc.perform(get("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.id').value(1))
                .andExpect(jsonPath('\$.label').value("Sushis (updated)"))
                .andExpect(jsonPath('\$.slow').value(true))
                .andExpect(jsonPath('\$.quick').value(true))
                .andExpect(jsonPath('\$.fromRestaurant').value(false))
                .andExpect(jsonPath('\$.vegan').value(true))
                .andExpect(jsonPath('\$.fish').value(true))
                .andExpect(jsonPath('\$.kidLunch').value(true))
                .andExpect(jsonPath("\$.months", hasSize(3)))
                .andExpect(jsonPath('\$.months[0]').value(1))
                .andExpect(jsonPath('\$.months[1]').value(2))
                .andExpect(jsonPath('\$.months[2]').value(3))
    }

    def "PUT /dishes/{dishId} - dish exists and empty months array sent - updates dish returns HTTP 204"() {
        when:
        def response = mvc.perform(put("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                    "label": "Sushis (updated)",
                    "slow": true,
                    "quick": true,
                    "fromRestaurant": false,
                    "vegan": true,
                    "fish": true,
                    "kidLunch": true,
                    "months": []
                }"""))

        then:
        response.andExpect(status().isOk())
    }

    def "GET /dishes/{dishId} - dish exists and has been updated - dish is now linked to all 12 months"() {
        when:
        def response = mvc.perform(get("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$.months", hasSize(12)))
    }

    def "GET /dishes/{dishId} - dish does not exist - returns HTTP 404"() {
        when:
        def response = mvc.perform(get("/dishes/1111111")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isNotFound())
    }

    def "PUT /meals/year/{year}/week/{weekNumber}/day/{day}/meal/{meal} - meal is in the past - returns HTTP 403"() {
        when:
        def response = mvc.perform(put("/meals/year/2026/week/1/day/MONDAY/meal/DINNER")
                .contentType(MediaType.APPLICATION_JSON).content("1"))

        then:
        response.andExpect(status().isForbidden())
    }

    def "PUT /meals/year/{year}/week/{weekNumber}/day/{day}/meal/{meal} - meal is in the future - returns HTTP 200 and updated meal plan"() {
        given:
        def now = LocalDateTime.now()
        def year = now.year + 1
        def week = now.get(WeekFields.of(Locale.FRANCE).weekOfYear())

        when:
        def response = mvc.perform(put("/meals/year/${year}/week/${week}/day/MONDAY/meal/DINNER")
                .contentType(MediaType.APPLICATION_JSON).content("12"))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath('\$.days[?(@.dayOfWeek == \'MONDAY\')].meals[?(@.type == \'DINNER\')].dish.id').value(12))
    }
}
