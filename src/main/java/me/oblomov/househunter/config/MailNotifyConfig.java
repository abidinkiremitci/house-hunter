package me.oblomov.househunter.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@ConfigurationProperties(
        prefix = "me.oblomov.househunter.mail.notify"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailNotifyConfig {

    private String sender;
    private List<String> recipients;
    private String mailTemplate;

    public String getTOs() {
        return String.join(", ", recipients);
    }

}
