package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification


class DishServiceSpec extends OnMangeQuoiSpec {

    def repositoryMock = Mock(DishRepository)

    def service = new DishService(repositoryMock)

    def "listDishes - returns list of dishes - returned list is not immutable"() {
        given:
        repositoryMock.findAll(_ as Specification, _ as Sort) >> [dishEntity1, dishEntity2]
        def dishes = service.listDishes(null)

        when:
        dishes.remove(Dish.from(dishEntity1))

        then:
        noExceptionThrown()

        and:
        dishes == [Dish.from(dishEntity2)]
    }

    def "listDishes - null criteria given - calls repository with an empty criteria instance and returns results"() {
        when:
        def result = service.listDishes(null)

        then:
        1 * repositoryMock.findAll(_ as Specification, _ as Sort) >> [dishEntity1, dishEntity2]

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
        1 * repositoryMock.findAll(specificationMock, _ as Sort) >> [dishEntity1, dishEntity2]

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

    def "addDish - new body sent - creates entity from body, calls repository to save it and returns DTO"() {
        given:
        def bodyMock = Mock(NewDish) {
            getLabel() >> "New label"
        }

        when:
        def result = service.addDish(bodyMock)

        then:
        1 * repositoryMock.save(_) >> { DishEntity e ->
            assert e.getLabel() == "New label"
            return e
        }

        and:
        result.getLabel() == "New label"
    }

    def "addDish - new body sent and repository throws DataIntegrityViolationException - DishAlreadyExistsException is thrown"() {
        given:
        def bodyMock = Mock(NewDish) {
            getLabel() >> "New label"
        }

        when:
        service.addDish(bodyMock)

        then:
        1 * repositoryMock.save(_) >> { throw new DataIntegrityViolationException("") }

        and:
        def e = thrown(DishAlreadyExistsException)
        e.getMessage() == "A dish with label 'New label' already exists"
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
        def result = service.updateDish(1, bodyMock)

        then:
        1 * repositoryMock.findById(1) >> Optional.of(dishEntity1)

        and:
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

        and:
        result.getLabel() == "Updated label"
    }
}
