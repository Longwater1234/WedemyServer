package com.davistiba.wedemyserver.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;

import java.util.Objects;

@Configuration
public class BraintreeConfig {
    private static final org.springframework.core.env.Environment env = new StandardEnvironment();

    private static final BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX, // <-- change if necessary (read the official docs!)
            Objects.requireNonNull(env.getProperty("BT_MERCHANT_ID")),
            Objects.requireNonNull(env.getProperty("BT_PUBLIC_KEY")),
            Objects.requireNonNull(env.getProperty("BT_PRIVATE_KEY"))
    );

    @Bean
    public static BraintreeGateway getGateway() {
        return gateway;
    }
}
