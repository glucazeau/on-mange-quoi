package com.sasagui.onmangequoi.mailing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class MailSender {

    private final RestClient restClient;

    private final MultiValueMap<String, String> mailBaseParameters = new LinkedMultiValueMap<>();

    @Getter
    @Setter
    private static class MailApiResponse {
        private String id;

        private String message;
    }

    public MailSender(RestClient restClient, MailingProperties properties, ObjectMapper objectMapper)
            throws JsonProcessingException {
        log.info(
                "Building email common parameters, with {} recipients configured",
                properties.getRecipients().size());
        this.restClient = restClient;
        Map<String, Map<String, String>> recipientVariables = properties.getRecipients().stream()
                .collect(Collectors.toMap(
                        MailingProperties.Recipient::getEmailAddress, r -> Map.of("name", r.getName())));
        String recipientAddresses = properties.getRecipients().stream()
                .map(MailingProperties.Recipient::getEmailAddress)
                .collect(Collectors.joining(","));
        Map.of(
                        "from", properties.getFrom(),
                        "h:Reply-To", properties.getReplyTo(),
                        "to", recipientAddresses,
                        "recipient-variables", objectMapper.writeValueAsString(recipientVariables))
                .forEach(mailBaseParameters::add);
    }

    public void send(String subject, String content) {
        log.info("Sending email with subject '{}'", subject);
        try {
            ResponseEntity<MailApiResponse> resp = restClient
                    .post()
                    .uri("/messages")
                    .body(buildBody(subject, content))
                    .retrieve()
                    .toEntity(MailApiResponse.class);
            log.info("Email successfully sent with ID {}", resp.getBody().getId());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error sending mail send request: {}", e.getMessage(), e);
            throw e;
        }
    }

    private MultiValueMap<String, String> buildBody(String subject, String body) {
        log.info("Adding subject and body to email body");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll(mailBaseParameters);
        params.put("subject", List.of(subject));
        params.put("html", List.of(body));
        return params;
    }
}
