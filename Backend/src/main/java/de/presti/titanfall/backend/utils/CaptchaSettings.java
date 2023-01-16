package de.presti.titanfall.backend.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@PropertySource("classpath:hidden.properties")
@ConfigurationProperties(prefix = "hcaptcha.sitekey")
public class CaptchaSettings {

    private String site;
    private String secret;
}