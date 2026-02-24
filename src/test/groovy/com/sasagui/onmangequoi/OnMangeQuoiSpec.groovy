package com.sasagui.onmangequoi

import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.dish.Dish
import com.sasagui.onmangequoi.dish.DishEntity
import com.sasagui.onmangequoi.meal.MealEntity
import com.sasagui.onmangequoi.meal.MealId
import com.sasagui.onmangequoi.meal.MealType
import java.time.DayOfWeek
import spock.lang.Shared
import spock.lang.Specification

class OnMangeQuoiSpec extends Specification {

    @Shared
    def weekMock = Mock(Week) {
        getYear() >> 2026
        getNumber() >> 12
        getPreviousWeek() >> new Week.WeekRef(2026, 11)
        getNextWeek() >> new Week.WeekRef(2026, 13)
    }

    @Shared
    def dishEntity1 = new DishEntity(id: 1, label: "Dish 1", slow: true, quick: true, fromRestaurant: true, vegan: true)

    def dishEntity2 = new DishEntity(id: 2, label: "Dish 2", slow: false, quick: false, fromRestaurant: false, vegan: false)

    def mealEntity1 = new MealEntity(new MealId(year: 2026, weekNumber: 12, dayOfWeek: DayOfWeek.MONDAY, mealType: MealType.DINNER), dishEntity1)

    @Shared
    def dish1 = Dish.from(dishEntity1)

    def dish2 = Dish.from(dishEntity2)
}
