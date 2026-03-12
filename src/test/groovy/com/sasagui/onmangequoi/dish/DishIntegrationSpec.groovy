package com.sasagui.onmangequoi.dish

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sasagui.onmangequoi.IntegrationSpec
import org.springframework.http.MediaType

class DishIntegrationSpec extends IntegrationSpec {

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
                            "vegan": false
                        }"""))

        then:
        response.andExpect(status().isCreated())
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

    def "PUT /dishes/{dishId} - dish exists - updates dish returns HTTP 200"() {
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
                    "kidLunch": true
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
    }

    def "GET /dishes/{dishId} - dish does not exist - returns HTTP 404"() {
        when:
        def response = mvc.perform(get("/dishes/1111111")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isNotFound())
    }

}
