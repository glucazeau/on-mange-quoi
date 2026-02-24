package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import org.springframework.data.jpa.domain.Specification


class DishServiceSpec extends OnMangeQuoiSpec {

    def repositoryMock = Mock(DishRepository)

    def service = new DishService(repositoryMock)

    def "listDishes - null criteria given - calls repository with an empty criteria instance and returns results"() {
        when:
        def result = service.listDishes(null)

        then:
        1 * repositoryMock.findAll(_ as Specification, _) >> [dishEntity1, dishEntity2]

        and:
        result == [Dish.from(dishEntity1), Dish.from(dishEntity2)]
    }

    def "listDishes - criteria given - calls repository with Specification from criteria and returns result"() {
        given:
        def criteriaSpy = Spy(new DishSearchCriteria())
        def specificationMock = Mock(Specification)

        when:
        def result = service.listDishes(criteriaSpy)

        then:
        1 * criteriaSpy.toSpec() >> specificationMock

        and:
        1 * repositoryMock.findAll(specificationMock, _) >> [dishEntity1, dishEntity2]

        and:
        result == [Dish.from(dishEntity1), Dish.from(dishEntity2)]
    }
}
