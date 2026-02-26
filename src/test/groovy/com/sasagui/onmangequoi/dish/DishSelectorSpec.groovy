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
        def result = selector.selectDish([dish1, dish2], Mock(Day), Mock(Meal), [] as Set, [])

        then: "each scorer is called twice"
        2 * dishScorerMock1.score(_ as DishScoringContext) >> { DishScoringContext ctx -> ctx.getDish() == dish1 ? -1 : 1 }
        2 * dishScorerMock2.score(_ as DishScoringContext) >> { DishScoringContext ctx -> ctx.getDish() == dish1 ? -1 : 1 }

        and: "dish with the highest score is returned"
        result == dish2
    }
}
