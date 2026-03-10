package com.sasagui.onmangequoi.dish

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.sasagui.onmangequoi.MvcSpecification
import org.spockframework.spring.SpringBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration


@ContextConfiguration(classes = [DishController, DishService])
class DishControllerSpec extends MvcSpecification {

    @SpringBean
    DishService dishServiceMock = Mock(DishService)

    def "GET /dishes - no request body sent - returns HTTP 200 and dishes JSON results"() {
        when:
        def response = mvc.perform(get("/dishes")
                .contentType(MediaType.APPLICATION_JSON))

        then: "calls service to get dishes"
        1 * dishServiceMock.listDishes(null) >> [dish1, dish2]

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$[0].label").value("Dish 1"))
                .andExpect(jsonPath("\$[0].slow").value(true))
                .andExpect(jsonPath("\$[0].quick").value(true))
                .andExpect(jsonPath("\$[0].fromRestaurant").value(true))
                .andExpect(jsonPath("\$[0].vegan").value(true))
                .andExpect(jsonPath("\$[0].fish").value(true))

                .andExpect(jsonPath("\$[1].label").value("Dish 2"))
                .andExpect(jsonPath("\$[1].slow").value(false))
                .andExpect(jsonPath("\$[1].quick").value(false))
                .andExpect(jsonPath("\$[1].fromRestaurant").value(false))
                .andExpect(jsonPath("\$[1].vegan").value(false))
                .andExpect(jsonPath("\$[1].fish").value(false))
    }

    def "GET /dishes/{dishId} - dish ID URL parameter - calls service with ID and returns HTTP 200 and dish JSON results"() {
        when:
        def response = mvc.perform(get("/dishes/1")
                .contentType(MediaType.APPLICATION_JSON))

        then: "calls service to get dishes"
        1 * dishServiceMock.getDish(1) >> dish1

        and:
        response.andExpect(status().isOk())
                .andExpect(jsonPath("\$.label").value("Dish 1"))
                .andExpect(jsonPath("\$.slow").value(true))
                .andExpect(jsonPath("\$.quick").value(true))
                .andExpect(jsonPath("\$.fromRestaurant").value(true))
                .andExpect(jsonPath("\$.vegan").value(true))
                .andExpect(jsonPath("\$.fish").value(true))
    }

    def "POST /dishes - new request body sent - returns HTTP 201"() {
        when:
        def response = mvc.perform(post("/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{
                            "label": "New label",
                            "slow": ${flagValue},
                            "quick": ${flagValue},
                            "fromRestaurant": ${flagValue},
                            "vegan": ${flagValue},
                            "fish": ${flagValue},
                            "kidLunch": ${flagValue}
                        }"""))

        then: "calls service to get dishes"
        1 * dishServiceMock.addDish(_) >> { NewDish nd ->
            assert nd.getLabel() == "New label"
            assert nd.isSlow() == flagValue
            assert nd.isQuick() == flagValue
            assert nd.isFromRestaurant() == flagValue
            assert nd.isVegan() == flagValue
            assert nd.isFish() == flagValue
            assert nd.isKidLunch() == flagValue
        }

        and:
        response.andExpect(status().isCreated())

        where:
        flagValue << [true, false]
    }
}
