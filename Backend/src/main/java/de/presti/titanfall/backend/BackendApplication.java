package de.presti.titanfall.backend;

import de.presti.titanfall.backend.utils.HashUtil;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.util.Arrays;
import java.util.Optional;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        Optional<String> authSecret = Arrays.stream(args).filter(s -> s.startsWith("authsecret=")).findFirst();

        if (authSecret.isPresent()) HashUtil.authSecret = authSecret.get().replace("authsecret=", "");

        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }
}
