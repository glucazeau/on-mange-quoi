package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec

class DishSpec extends OnMangeQuoiSpec {

    def "availableAllYear - dish is available for #monthsValue.size() months - returns #expected"() {
        given:
        def dish = Dish.from(new DishEntity("months": monthsValue))

        expect:
        dish.availableAllYear() == expected

        where:
        monthsValue      | expected
        (1..12).toList() | true
        (1..11).toList() | false
        (1..10).toList() | false
        (1..9).toList()  | false
        (1..8).toList()  | false
        (1..7).toList()  | false
        (1..6).toList()  | false
        (1..5).toList()  | false
        (1..4).toList()  | false
        (1..3).toList()  | false
        (1..2).toList()  | false
        (1..1).toList()  | false
    }
}
