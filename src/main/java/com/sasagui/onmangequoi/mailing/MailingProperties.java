package com.sasagui.onmangequoi.mailing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Component
@ConfigurationProperties(prefix = "onmangequoi.mailing")
public class MailingProperties {

    @NotBlank(message = "Mail sender cannot be empty")
    private String from;

    @NotBlank(message = "Mail reply-to address cannot be empty")
    private String replyTo;

    @NotEmpty(message = "Mail recipient(s) must be configured")
    private List<Recipient> recipients;

    @ToString.Include
    @NotEmpty(message = "Mail API base URL cannot be empty")
    private String mailApiBaseUrl;

    @NotEmpty(message = "Mail API key cannot be empty")
    private String mailApiKey;

    @Getter
    @Setter
    public static class Recipient {
        private String emailAddress;

        private String name;
    }
}
