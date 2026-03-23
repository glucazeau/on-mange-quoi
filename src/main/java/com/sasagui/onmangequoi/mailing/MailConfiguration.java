package com.sasagui.onmangequoi.mailing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Slf4j
@Configuration
@AllArgsConstructor
public class MailConfiguration {

    private final MailingProperties properties;

    @Bean
    public RestClient restClient() {
        log.info("Configuring mailing RestClient from mailing properties {}", properties);
        return RestClient.builder()
                .baseUrl(properties.getMailApiBaseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Basic "
                                + java.util.Base64.getEncoder()
                                        .encodeToString(String.format("api:%s", properties.getMailApiKey())
                                                .getBytes()))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        log.info("Configuring ObjectMapper");
        return new ObjectMapper();
    }
}
