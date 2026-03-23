package com.sasagui.onmangequoi.mailing

import static com.github.tomakehurst.wiremock.client.WireMock.*

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.sasagui.onmangequoi.OnMangeQuoiSpec
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import spock.lang.Shared

class MailSenderSpec extends OnMangeQuoiSpec {

    @Shared
    def objectMapperMock = new ObjectMapper()

    @Shared
    MailingProperties properties = new MailingProperties()

    @Shared
    def mailSender

    def setupSpec() {
        def wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort().notifier(new ConsoleNotifier(true)))
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
        def wireMockServerUrl = "http://localhost:${wireMockServer.port()}"

        properties.from = "Joe <joe@example.fr>"
        properties.replyTo = "reply@example.com"
        properties.mailApiBaseUrl = wireMockServerUrl
        properties.mailApiKey = "aaa-bbb-ccc"
        properties.recipients = [
                new MailingProperties.Recipient("name": "Bob", "emailAddress": "bob@example.com"),
                new MailingProperties.Recipient("name": "John", "emailAddress": "john@example.com"),
        ]

        def restClient = new MailConfiguration(properties).restClient()
        mailSender =  new MailSender(restClient, properties, objectMapperMock)
    }

    def "send - subject and body given - calls mail API with expected parameters"() {
        given:
        stubFor(post(urlPathEqualTo("/messages"))
                .withHeader(HttpHeaders.CONTENT_TYPE, containing(MediaType.MULTIPART_FORM_DATA_VALUE))
                .withBasicAuth("api", "aaa-bbb-ccc")
                .withMultipartRequestBody(aMultipart("from").withBody(equalTo("Joe <joe@example.fr>")))
                .withMultipartRequestBody(aMultipart("h:Reply-To").withBody(equalTo("reply@example.com")))
                .withMultipartRequestBody(aMultipart("to").withBody(equalTo("bob@example.com,john@example.com")))
                .withMultipartRequestBody(aMultipart("recipient-variables").withBody(equalToJson("{\"bob@example.com\": {\"name\": \"Bob\"}, \"john@example.com\": {\"name\": \"John\"}}")))

                .willReturn(aResponse().withStatus(200).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).withBody('{"id": "1234", "message": "Queued. Thank you."}'))
        )

        when:
        mailSender.send("The subject", "The body")

        then:
        noExceptionThrown()
    }

    def "send - mail API returns response with HTTP status code #httpCode - exception is thrown"() {
        given:
        stubFor(post(urlPathEqualTo("/messages"))
                .willReturn(aResponse().withStatus(httpCode))
        )

        when:
        mailSender.send("The subject", "The body")

        then:
        thrown(exceptionClass)

        where:
        httpCode | exceptionClass
        400      | HttpClientErrorException
        500      | HttpServerErrorException
    }
}
