package com.sasagui.onmangequoi.dish

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
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

}
