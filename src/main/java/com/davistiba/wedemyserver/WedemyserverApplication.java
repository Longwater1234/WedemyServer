/*
 * Copyright (c) 2021. Davis Tibbz.  MIT License. Github: https://github.com/longwater1234
 */

package com.davistiba.wedemyserver;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableCaching
public class WedemyserverApplication {

    @Value(value = "${frontend.root.url}")
    private String FRONTEND_URL;

    public static void main(String[] args) {
        SpringApplication.run(WedemyserverApplication.class, args);
    }

    @Bean
    // required for Heroku
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .exposedHeaders("*")
                        .maxAge(3600L)
                        .allowedOriginPatterns("http://localhost:[*]", FRONTEND_URL)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperCustomizer() {
        return builder -> {
            // formatter
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // deserializers (read JSON)
            builder.deserializers(new LocalDateDeserializer(df));
            builder.deserializers(new LocalDateTimeDeserializer(dtf));

            // serializers (write JSON)
            builder.serializers(new LocalDateSerializer(df));
            builder.serializers(new LocalDateTimeSerializer(dtf));
        };
    }
}
