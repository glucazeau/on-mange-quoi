package com.sasagui.onmangequoi.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DishScorerConfig {

    @Bean
    public DishScorer quickDishOnWeekDayScorer() {
        return context -> {
            log.debug("Scoring quick dish and week day");
            if (context.getDish().isQuick() && !context.getDay().isWeekend()) {
                return 1;
            }
            return 0;
        };
    }
}
