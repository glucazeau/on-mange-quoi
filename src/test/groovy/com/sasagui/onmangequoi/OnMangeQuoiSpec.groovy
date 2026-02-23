package com.sasagui.onmangequoi

import com.sasagui.onmangequoi.calendar.Week
import com.sasagui.onmangequoi.dish.Dish
import com.sasagui.onmangequoi.dish.DishEntity
import com.sasagui.onmangequoi.meal.MealEntity
import com.sasagui.onmangequoi.meal.MealId
import com.sasagui.onmangequoi.meal.MealType
import java.time.DayOfWeek
import java.time.Year
import spock.lang.Specification

class OnMangeQuoiSpec extends Specification {

    def weekMock = Mock(Week) {
        getYear() >> Year.of(2026)
        getNumber() >> 12
    }

    def dishEntity1 = new DishEntity(id: 1, label: "Dish 1", slow: true, quick: true, fromRestaurant: true, vegan: true)

    def dishEntity2 = new DishEntity(id: 2, label: "Dish 2", slow: false, quick: false, fromRestaurant: false, vegan: false)

    def mealEntity1 = new MealEntity(new MealId(year: 2026, weekNumber: 12, dayOfWeek: DayOfWeek.MONDAY, mealType: MealType.DINNER), dishEntity1)

    def dish1 = Dish.from(dishEntity1)

    def dish2 = Dish.from(dishEntity2)
}
