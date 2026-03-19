package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.meal.Meal
import java.time.DayOfWeek

class DishScoringContextSpec extends OnMangeQuoiSpec {

    def restaurantDish1 = Mock(Dish) {
        isFromRestaurant() >> true
    }

    def restaurantDish2 = Mock(Dish) {
        isFromRestaurant() >> true
    }

    def restaurantDish3 = Mock(Dish) {
        isFromRestaurant() >> true
    }

    def context = new DishScoringContext(Mock(Dish), Mock(Day), Mock(Meal), [restaurantDish1] as Set, [restaurantDish2, restaurantDish3] as Set, [] as Set)

    def "countCurrentWeekRestaurants - current week has 1 dish from restaurant - returns 1"() {
        expect:
        context.countCurrentWeekRestaurants() == 1
    }

    def "countLastWeekRestaurants - last week had 2 dishes from restaurant - returns 2"() {
        expect:
        context.countLastWeekRestaurants() == 2
    }
}
