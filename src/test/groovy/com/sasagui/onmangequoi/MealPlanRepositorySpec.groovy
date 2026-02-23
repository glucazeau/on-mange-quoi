package com.sasagui.onmangequoi

import java.time.DayOfWeek
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
class MealPlanRepositorySpec extends OnMangeQuoiSpec {

    @Autowired
    MealPlanRepository repository

    def "save - new entity created from MealPlan DTO - entity successfully saved"() {
        given:
        MealPlanEntity mealPlanEntity = new MealPlanEntity(new MealPlanId(2026, 12))

        def dishEntity = new DishEntity()
        mealPlanEntity.addMeal(new MealEntity(new MealId(2026, 12, DayOfWeek.MONDAY, MealType.LUNCH), dishEntity))

        when:
        def result = repository.save(mealPlanEntity)

        then: "meal plan ID is correctly saved"
        result.getId() == new MealPlanId(2026, 12)

        and: "meal plan staus is correctly initialized"
        result.getStatus() == MealPlanStatus.PENDING_APPROVAL

        and: "meal plan contains one meal"
        result.getMeals().size() == 1

        and: "meal ID is correctly saved"
        result.getMeals()[0].getId() == new MealId(2026, 12, DayOfWeek.MONDAY, MealType.LUNCH)

        and:
        def savedPlan = repository.findById(new MealPlanId(2026, 12))
        savedPlan.isPresent()
    }
}
