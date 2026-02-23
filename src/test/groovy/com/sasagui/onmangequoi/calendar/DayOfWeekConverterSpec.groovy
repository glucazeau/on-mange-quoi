package com.sasagui.onmangequoi.calendar

import static java.time.DayOfWeek.*

import com.sasagui.onmangequoi.OnMangeQuoiSpec

class DayOfWeekConverterSpec extends OnMangeQuoiSpec {

    def converter = new DayOfWeekConverter()

    def "convertToDatabaseColumn - given day is #givenDay - returns #expectedValue"() {
        expect:
        converter.convertToDatabaseColumn(givenDay) == expectedValue

        where:
        givenDay  | expectedValue
        MONDAY    | 1
        TUESDAY   | 2
        WEDNESDAY | 3
        THURSDAY  | 4
        FRIDAY    | 5
        SATURDAY  | 6
        SUNDAY    | 7
    }

    def "convertToEntityAttribute - given value is #givenValue - returns #expectedValue"() {
        expect:
        converter.convertToEntityAttribute(givenValue) == expectedValue

        where:
        givenValue | expectedValue
        1          | MONDAY
        2          | TUESDAY
        3          | WEDNESDAY
        4          | THURSDAY
        5          | FRIDAY
        6          | SATURDAY
        7          | SUNDAY
    }
}
