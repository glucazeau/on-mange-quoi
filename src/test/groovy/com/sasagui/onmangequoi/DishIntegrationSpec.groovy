package com.sasagui.onmangequoi

import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.http.MediaType

class DishIntegrationSpec extends IntegrationSpec {

    def "GET /dishes - no request body sent - returns HTTP 200 and dishes JSON results"() {
        when:
        def response = mvc.perform(post("/dishes")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$", hasSize(29)))
    }
}
