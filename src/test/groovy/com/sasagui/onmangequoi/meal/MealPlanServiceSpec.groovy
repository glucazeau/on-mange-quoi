package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.dish.DishRepository
import java.time.LocalDate

class MealPlanServiceSpec extends OnMangeQuoiSpec {

    def mealPlanRepositoryMock = Mock(MealPlanRepository)

    def dishRepositoryMock = Mock(DishRepository)

    def service = new MealPlanService(mealPlanRepositoryMock, dishRepositoryMock)

    def "getMealPlan - a meal plan exists for the given year and week - returns meal plan found"() {
        given:
        def mealPlanEntity = new MealPlanEntity(new MealPlanId(year: 2026, weekNumber: 12))
        mealPlanEntity.addMeal(mealEntity1)

        when:
        def result = service.getMealPlan(weekMock)

        then: "meal plan repository is called with year and week number"
        1 * mealPlanRepositoryMock.findById(new MealPlanId(2026, 12)) >> Optional.of(mealPlanEntity)

        and: "result is not empty"
        result.isPresent()
        def mealPlan = result.get()

        and: "week is correctly built"
        mealPlan.getWeek().getYear() == 2026
        mealPlan.getWeek().getNumber() == 12

        and: "days and meals are correctly set"
        mealPlan.getDays()[0].getMeals()[0].getDish().getId() == 1

        and: "date is set on day"
        mealPlan.getDays()[0].getDate() == LocalDate.of(2026,03, 16)
    }

    def "getMealPlan - no meal plan exist for the given year and week - returns Optional empty"() {
        when:
        def result = service.getMealPlan(weekMock)

        then: "meal plan repository is called with year and week number"
        1 * mealPlanRepositoryMock.findById(new MealPlanId(2026, 12)) >> Optional.empty()

        and: "result is empty"
        result.isEmpty()
    }

    def "saveMealPlan - DTO given - builds and save entity"() {
        given:
        def mealPlan = MealPlan.schoolWeek(weekMock)
        mealPlan.getDays()[0].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[1].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[2].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[2].getMeals()[1].setDish(dish2)
        mealPlan.getDays()[3].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[4].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[5].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[5].getMeals()[1].setDish(dish2)
        mealPlan.getDays()[6].getMeals()[0].setDish(dish1)
        mealPlan.getDays()[6].getMeals()[1].setDish(dish2)

        when:
        service.saveMealPlan(mealPlan)

        then: "dish repository called to load dish references"
        7 * dishRepositoryMock.getReferenceById(1) >> dishEntity1
        3 * dishRepositoryMock.getReferenceById(2) >> dishEntity2

        and:
        mealPlanRepositoryMock.save(_ as MealPlanEntity) >> { MealPlanEntity e ->
            e.getMeals().size() == 10
            e.getMeals().every({ it.getDish() in [dishEntity1, dishEntity2]})
            e.getMeals().every({it -> it.getId().year == 2026 && it.getId().weekNumber == 12 })
            return e
        }
    }

}
