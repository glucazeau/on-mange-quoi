package com.sasagui.onmangequoi.meal;

import com.sasagui.onmangequoi.mailing.MailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Component
@AllArgsConstructor
public class MealPlanEventListener {

    private final MailSender mailSender;

    private final ITemplateEngine templateEngine;

    @EventListener
    public void handleNewMealPlanEvent(final NewMealPlanEvent event) {
        log.info("New meal plan event received for week {}", event.getMealPlan().getWeek());
        mailSender.send(getSubject(event.getMealPlan()), getBody(event.getMealPlan()));
    }

    private String getSubject(MealPlan mealPlan) {
        return String.format(
                "On mange quoi semaine %s ? \uD83C\uDF54 \uD83C\uDF55 \uD83E\uDD58 \uD83E\uDD57",
                mealPlan.getWeek().getNumber());
    }

    private String getBody(MealPlan mealPlan) {
        Context context = new Context();
        context.setVariable("mealPlan", mealPlan);
        return templateEngine.process("meal-plan-mail", context);
    }
}
