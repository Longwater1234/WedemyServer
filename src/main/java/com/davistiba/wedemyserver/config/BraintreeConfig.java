package com.davistiba.wedemyserver.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * READ OFFICIAL DOCS
 * https://developer.paypal.com/braintree/docs/start/hello-server/java
 */
@Configuration
public class BraintreeConfig {
    @Value("${BT_MERCHANT_ID:sandbox_merchant}")
    private String merchantId;

    @Value("${BT_PUBLIC_KEY:sandbox_public_key}")
    private String publicKey;

    @Value("${BT_PRIVATE_KEY:sandbox_private_key}")
    private String privateKey;

    @Bean
    public BraintreeGateway getGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX, //<--(dev mode)
                merchantId,
                publicKey,
                privateKey
        );
    }
}
