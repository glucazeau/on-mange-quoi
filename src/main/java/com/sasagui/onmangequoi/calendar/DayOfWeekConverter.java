package com.sasagui.onmangequoi.calendar;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.DayOfWeek;
import org.springframework.stereotype.Component;

@Component
@Converter
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final DayOfWeek attribute) {
        return attribute.getValue();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(final Integer dbData) {
        return DayOfWeek.of(dbData);
    }
}
