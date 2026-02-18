package com.sasagui.onmangequoi

import org.springframework.data.jpa.domain.Specification


class DishServiceSpec extends OnMangeQuoiSpec {

    def repositoryMock = Mock(DishRepository)

    def service = new DishService(repositoryMock)

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
