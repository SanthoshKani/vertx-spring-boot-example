package io.vertx.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import io.vertx.core.Vertx;

@SpringBootApplication
@PropertySource(value = "classpath:vertx.properties")
public class ApplicationInitializer {

    @Autowired
    public Environment environment;

    @Bean
    public Vertx vertx() {
        return Vertx.factory.vertx();
    }

    public static void main(String... args) {
        SpringApplication.run(ApplicationInitializer.class, args);
    }
}
