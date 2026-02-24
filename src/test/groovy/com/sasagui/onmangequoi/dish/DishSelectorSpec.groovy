package com.sasagui.onmangequoi.dish

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.calendar.Day
import com.sasagui.onmangequoi.meal.Meal

class DishSelectorSpec extends OnMangeQuoiSpec {

    def dishScorerMock1 = Mock(DishScorer)
    def dishScorerMock2 = Mock(DishScorer)

    def selector = new DishSelector([dishScorerMock1, dishScorerMock2])

    def "selectDish - list of dishes, day and meal given - calls each scorers for each dish and returns the highest scored"() {
        when:
        def result = selector.selectDish([dish1, dish2], Mock(Day), Mock(Meal), [], [])

        then: "each scorer is called twice"
        1 * dishScorerMock1.score(dish1, _ as Day, _ as Meal, [], []) >> -1
        1 * dishScorerMock1.score(dish2, _ as Day, _ as Meal, [], []) >> 1

        1 * dishScorerMock2.score(dish1, _ as Day, _ as Meal, [], []) >> -1
        1 * dishScorerMock2.score(dish2, _ as Day, _ as Meal, [], []) >> 1

        and: "dish with the highest score is returned"
        result == dish2
    }
}
