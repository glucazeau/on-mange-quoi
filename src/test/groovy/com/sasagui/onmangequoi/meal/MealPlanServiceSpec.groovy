package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.dish.Dish
import com.sasagui.onmangequoi.dish.DishEntity
import com.sasagui.onmangequoi.dish.DishRepository
import java.time.LocalDate

class MealPlanServiceSpec extends OnMangeQuoiSpec {

    def mealRepositoryMock = Mock(MealRepository)

    def dishRepositoryMock = Mock(DishRepository)

    def service = new MealPlanService(mealRepositoryMock, dishRepositoryMock)

    def "getMealPlan - a meal plan exists for the given year and week - returns meal plan found"() {
        given:
        SpyStatic(MealRepository)
        def specificationMock = Mock(org.springframework.data.jpa.domain.Specification)

        when:
        def result = service.getMealPlan(weekMock)

        then:
        1 * MealRepository.fromWeek(weekMock) >> specificationMock

        then: "meal plan repository is called with year and week number"
        1 * mealRepositoryMock.findAll(specificationMock) >> [mealEntity1]

        and: "week is correctly built"
        result.getWeek().getYear() == 2026
        result.getWeek().getNumber() == 12

        and: "days and meals are correctly set"
        result.getDays()[0].getMeals()[0].getDish().getId() == 1

        and: "date is set on day"
        result.getDays()[0].getDate() == LocalDate.of(2026,03, 16)
    }

    def "saveMealPlan - given meal plan contains 2 meals with a dish - saves 2 meal entities"() {
        given:
        def mealMock1 = Mock(Meal) {
            getDish() >> dish1
        }
        def mealMock2 = Mock(Meal) {
            getDish() >> dish2
        }
        def mealPlanMock = Mock(MealPlan) {
            getMeals() >> [mealMock1, mealMock2]
            getWeek() >> weekMock
        }

        when:
        service.saveMealPlan(mealPlanMock)

        then:
        1 * dishRepositoryMock.getReferenceById(1) >> dishEntity1
        1 * dishRepositoryMock.getReferenceById(2) >> dishEntity2

        and:
        mealRepositoryMock.saveAll(_ as List<MealEntity>) >> { params ->
            List<MealEntity> entities = params[0]
            assert entities.size() == 2
            assert entities.every {it.id.year == 2026 }
            assert entities.every {it.id.weekNumber == 12 }
            assert entities[0].getDish() == dishEntity1
            assert entities[1].getDish() == dishEntity2
        }
    }

    def "saveMealPlan - given meal plan contains 1 meal with an empty dish - meal with empty dish is not saved"() {
        given:
        def mealMock1 = Mock(Meal) {
            getDish() >> Dish.empty()
        }
        def mealMock2 = Mock(Meal) {
            getDish() >> dish2
        }
        def mealPlanMock = Mock(MealPlan) {
            getMeals() >> [mealMock1, mealMock2]
            getWeek() >> weekMock
        }

        when:
        service.saveMealPlan(mealPlanMock)

        then: "empty dish is ignored when loading entities"
        0 * dishRepositoryMock.getReferenceById(-1)

        and:
        1 * dishRepositoryMock.getReferenceById(2) >> dishEntity2

        and:
        mealRepositoryMock.saveAll(_ as List<MealEntity>) >> { params ->
            List<MealEntity> entities = params[0]
            assert entities.size() == 1
            assert entities.every {it.id.year == 2026 }
            assert entities.every {it.id.weekNumber == 12 }
            assert entities[0].getDish() == dishEntity2
        }
    }
}
