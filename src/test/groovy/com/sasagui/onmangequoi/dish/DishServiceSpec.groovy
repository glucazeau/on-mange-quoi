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

    def "getDish - dish ID given - loads entity with ID and returns DTO"() {
        when:
        def result = service.getDish(1)

        then:
        1 * repositoryMock.getReferenceById(1) >> dishEntity1

        and:
        result == dish1
    }

    def "addDish - new body sent - creates entity from body and calls repository to save it"() {
        given:
        def bodyMock = Mock(NewDish) {
            getLabel() >> "New label"
        }

        when:
        service.addDish(bodyMock)

        then:
        1 * repositoryMock.save(_) >> { DishEntity e ->
            assert e.getLabel() == "New label"
            return e
        }
    }

    def "updateDish - ID and new body sent - loads entity from ID, updates and calls repository to save it"() {
        given:
        def bodyMock = Mock(NewDish) {
            getLabel() >> "Updated label"
            isSlow() >> true
            isQuick() >> false
            isFromRestaurant() >> false
            isVegan() >> false
            isFish() >> true
            isKidLunch() >> false
        }

        when:
        service.updateDish(1, bodyMock)

        then:
        1 * repositoryMock.getReferenceById(1) >> dishEntity1

        1 * repositoryMock.save(dishEntity1) >> { DishEntity e ->
            assert e.getLabel() == "Updated label"
            assert e.isSlow()
            assert !e.isQuick()
            assert !e.isFromRestaurant()
            assert !e.isVegan()
            assert e.isFish()
            assert !e.isKidLunch()
            return e
        }
    }
}
