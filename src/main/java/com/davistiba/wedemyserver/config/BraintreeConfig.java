package com.davistiba.wedemyserver.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;

import java.util.Objects;

@Configuration
public class BraintreeConfig {
    //get ENV values
    private static final org.springframework.core.env.Environment ENV = new StandardEnvironment();

    private static final BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX, // <-- (TEST mode, no actual charges incurred)
            Objects.requireNonNull(ENV.getProperty("BT_MERCHANT_ID")),
            Objects.requireNonNull(ENV.getProperty("BT_PUBLIC_KEY")),
            Objects.requireNonNull(ENV.getProperty("BT_PRIVATE_KEY"))
    );

    @Bean
    public static BraintreeGateway getGateway() {
        return gateway;
    }
}
