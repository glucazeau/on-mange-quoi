package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@DataJpaTest
class DishRepositorySpec extends OnMangeQuoiSpec {

    @Autowired
    DishRepository repository

    def "findAll - criteria with #field #value - returns expected results"() {
        given:
        def criteria = new DishSearchCriteria()
        criteria."$field" = value

        when:
        def results = repository.findAll(criteria.toSpec())

        then:
        if (value != null) {
            results.size() == 8
        } else {
            results.size() == 16
        }

        and:
        if (value != null) {
            results.every { it.slow == value }
        }
        where:
        [field, value, expectedSize] << [
                ["slow", "quick", "fromRestaurant", "vegan"],
                [true, false]
        ].combinations()
    }

    def "findAll - criteria with label #value - returns expected results"() {
        given:
        def criteria = new DishSearchCriteria()
        criteria.label = value

        when:
        def results = repository.findAll(criteria.toSpec())

        then:
        results.size() == expectedSize

        where:
        value    | expectedSize
        "Di"     | 16
        "Dish 1" | 8
    }
}
