package com.davistiba.wedemyserver.controllers;

import com.braintreegateway.*;
import com.davistiba.wedemyserver.config.BraintreeConfig;
import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.services.CheckoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/checkout")
@Secured(value = "ROLE_USER")
public class CheckoutController {

    private final BraintreeGateway gateway;

    private final CheckoutService checkoutService;

    private final UserRepository userRepository;

    public final Logger logger = LoggerFactory.getLogger(String.valueOf(this));

    @Autowired
    public CheckoutController(BraintreeGateway gateway, CheckoutService checkoutService, UserRepository userRepository) {
        this.gateway = BraintreeConfig.getGateway();
        this.checkoutService = checkoutService;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/token")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> getClientToken() {
        try {
            Map<String, String> response = new HashMap<>();
            String clientToken = gateway.clientToken().generate();
            response.put("clientToken", clientToken);
            logger.info("TOKEN:: Now in thread {}", Thread.currentThread().getName());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @PostMapping(path = "/complete")
    public ResponseEntity<Map<String, Object>> completePurchase(@Valid @RequestBody CheckoutRequest checkout,
                                                                @NotNull HttpSession session) {
        String transactionId;
        Map<String, Object> response;
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        List<String> errorList;

        // try to create Braintree transaction
        TransactionRequest transactionRequest = new TransactionRequest()
                .amount(checkout.getTotalAmount())
                .paymentMethodNonce(checkout.getNonce())
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);

        if (result.isSuccess()) {
            transactionId = result.getTarget().getId();
        } else if (result.getTransaction() != null) {
            transactionId = result.getTransaction().getId();
        } else {
            errorList = new ArrayList<>();
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorList.add(error.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorList.toString());
        }

        //OK SO FAR, ALL IS GOOD. let's finally make DB queries and wrap everything.

        try {
            User user = userRepository.findById(userId).orElseThrow();
            logger.info("COMPLETE:: Now in thread {}", Thread.currentThread().getName());
            response = checkoutService.processCheckoutDatabase(transactionId, userId, checkout, user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not complete purchase", e);
        }
    }


}
