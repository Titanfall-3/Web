package de.presti.titanfall.backend.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class HashUtil {

    @Bean
    public static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(-1, new SecureRandom());
    }
}
