package com.sasagui.onmangequoi.meal

import com.sasagui.onmangequoi.OnMangeQuoiSpec
import com.sasagui.onmangequoi.mailing.MailSender
import org.thymeleaf.ITemplateEngine
import org.thymeleaf.context.Context

class MealPlanEventListenerSpec extends OnMangeQuoiSpec {

    def mailSenderMock = Mock(MailSender)

    def templateEngineMock = Mock(ITemplateEngine)

    def listener = new MealPlanEventListener(mailSenderMock, templateEngineMock)

    def "handleNewMealPlanEvent - event with meal plan given - computes email subject and body from meal plan and calls mail sender"() {
        given:
        def mealPlanMock = Mock(MealPlan) {
            getWeek() >> weekMock
        }
        def event = new NewMealPlanEvent(mealPlanMock)

        when:
        listener.handleNewMealPlanEvent(event)

        then:
        1 * templateEngineMock.process("meal-plan-mail", _ as Context) >> "HTML"

        and:
        1 * mailSenderMock.send("On mange quoi semaine 12 ? 🍔 🍕 🥘 🥗", "HTML")
    }
}
