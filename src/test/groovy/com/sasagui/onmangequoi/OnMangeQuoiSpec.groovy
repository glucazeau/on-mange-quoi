package com.sasagui.onmangequoi

import spock.lang.Specification

class OnMangeQuoiSpec extends Specification {

    def dishEntity1 = new DishEntity(id: 1, label: "Dish 1", slow: true, quick: true, fromRestaurant: true, vegan: true)

    def dishEntity2 = new DishEntity(id: 2, label: "Dish 2", slow: false, quick: false, fromRestaurant: false, vegan: false)

    def dish1 = Dish.from(dishEntity1)

    def dish2 = Dish.from(dishEntity2)
}
