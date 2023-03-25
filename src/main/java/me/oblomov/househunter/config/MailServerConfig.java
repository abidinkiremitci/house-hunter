package me.oblomov.househunter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        prefix = "me.oblomov.househunter.mail.server"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailServerConfig {
    private String host;
    private int port;
    private boolean authorizationRequired;
    private String factory;
    private String username;
    private String password;
}
