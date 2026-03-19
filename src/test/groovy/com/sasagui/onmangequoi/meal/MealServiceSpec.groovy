package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.dish.DishRepository
import java.time.DayOfWeek
import java.time.LocalDate

class MealServiceSpec extends OnMangeQuoiSpec {

    def mealRepositoryMock = Mock(MealRepository)

    def dishRepositoryMock = Mock(DishRepository)

    def mealService = new MealService( mealRepositoryMock, dishRepositoryMock)

    def "putMeal - day is in the past - throws exception"() {
        given:
        def weekMock = Mock(Week) {
            getYear() >> 2023
            getNumber() >> 1
            getStart() >> LocalDate.of(2023, 1, 1)
        }

        when:
        mealService.putMeal(weekMock, DayOfWeek.FRIDAY, MealType.DINNER, 1)

        then:
        def e = thrown(MealNotEditableException)
        e.getMessage() == String.format("Meal for week %s, %s and type %s is in the past", weekMock, DayOfWeek.FRIDAY, MealType.DINNER)
    }

    def "putMeal - day is not in the past and meal is already set - loads dish and updates the meal entity"() {
        given:
        def mealEntitySpy = Spy(MealEntity)

        when:
        mealService.putMeal(weekMock, DayOfWeek.FRIDAY, MealType.DINNER, 1)

        then:
        1 * dishRepositoryMock.getReferenceById(1) >> dishEntity1

        and:
        1 * mealRepositoryMock.findById(_ as MealId) >> { MealId id ->
            assert id.getYear() == 2026
            assert id.getWeekNumber() == 12
            assert id.getDayOfWeek() == DayOfWeek.FRIDAY
            assert id.getMealType() == MealType.DINNER
            return Optional.of(mealEntitySpy)
        }

        and:
        1 * mealRepositoryMock.saveAndFlush(_ as MealEntity) >> { MealEntity m ->
            assert m.getDish() == dishEntity1
        }
    }

    def "putMeal - day is not in the past and meal is not set yet - loads dish and saves a new meal entity"() {
        when:
        mealService.putMeal(weekMock, DayOfWeek.FRIDAY, MealType.DINNER, 1)

        then:
        1 * dishRepositoryMock.getReferenceById(1) >> dishEntity1

        and:
        1 * mealRepositoryMock.findById(_ as MealId) >> { MealId id ->
            assert id.getYear() == 2026
            assert id.getWeekNumber() == 12
            assert id.getDayOfWeek() == DayOfWeek.FRIDAY
            assert id.getMealType() == MealType.DINNER
            return Optional.empty()
        }

        and:
        1 * mealRepositoryMock.saveAndFlush(_ as MealEntity) >> { MealEntity m ->
            assert m.getId().getYear() == 2026
            assert m.getId().getWeekNumber() == 12
            assert m.getId().getDayOfWeek() == DayOfWeek.FRIDAY
            assert m.getId().getMealType() == MealType.DINNER
            assert m.getDish() == dishEntity1
        }
    }
}
