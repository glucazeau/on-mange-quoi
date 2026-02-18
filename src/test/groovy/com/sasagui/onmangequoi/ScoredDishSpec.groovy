package com.sasagui.onmangequoi

class ScoredDishSpec extends OnMangeQuoiSpec {

    def "compareTo - this score is #score1 and other score is #score2 - result is #expected"() {
        given:
        def sd1 = new ScoredDish(dish1)
        sd1.setScore(score1)

        def sd2 = new ScoredDish(dish2)
        sd2.setScore(score2)

        expect:
        sd1.compareTo(sd2) == expected

        where:
        score1 | score2 | expected
        10     | 5      | 1
        5      | 10     | -1
        5      | 5      | 0
        -5     | -10    | 1
        -10    | 5      | -1
        -5     | -5     | 0
    }
}
