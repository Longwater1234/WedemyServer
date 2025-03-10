package com.davistiba.wedemyserver.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;

import java.util.Objects;

/*
 * READ OFFICIAL DOCS
 * https://developer.paypal.com/braintree/docs/start/hello-server/java
 */
@Configuration
public class BraintreeConfig {
    //Get ENV variables
    private static final org.springframework.core.env.Environment ENV = new StandardEnvironment();

    @Bean
    public BraintreeGateway getGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX, //<--(dev mode)
                //ensure not NULL!
                Objects.requireNonNull(ENV.getProperty("BT_MERCHANT_ID"), "BT_MERCHANT_ID is null"),
                Objects.requireNonNull(ENV.getProperty("BT_PUBLIC_KEY"), "BT_PUBLIC_KEY is null"),
                Objects.requireNonNull(ENV.getProperty("BT_PRIVATE_KEY"), "BT_PRIVATE_KEY is null")
        );
    }
}
