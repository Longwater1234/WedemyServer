package com.davistiba.wedemyserver.controllers;

import com.braintreegateway.*;
import com.davistiba.wedemyserver.config.BraintreeConfig;
import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.UserRepository;
import com.davistiba.wedemyserver.service.CheckoutService;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THIS MODIFIED CODE TAKEN FROM THE OFFICIAL Braintree Github
 * https://github.com/braintree/braintree_spring_example
 */
@RestController
@RequestMapping(path = "/checkout", produces = MediaType.APPLICATION_JSON_VALUE)
@Secured(value = "ROLE_STUDENT")
public class CheckoutController {

    private final BraintreeGateway gateway;

    private final CheckoutService checkoutService;

    private final UserRepository userRepository;

    @Autowired
    public CheckoutController(BraintreeGateway gateway, CheckoutService checkoutService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.gateway = gateway;
        this.checkoutService = checkoutService;
    }

    @GetMapping(path = "/token")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> getClientToken() {
        Map<String, String> response = new HashMap<>(1);
        String clientToken = gateway.clientToken().generate();
        response.put("clientToken", clientToken);
        return response;
    }


    @PostMapping(path = "/complete")
    @CacheEvict(value = "student-summary", key = "#session.id")
    public ResponseEntity<MyCustomResponse> completePurchase(@Valid @RequestBody CheckoutRequest request,
                                                             HttpSession session) {

        final Integer userId = MyUserDetailsService.getSessionUserId(session);
        final User user = userRepository.findById(userId).orElseThrow();

        // try to create Braintree transaction
        TransactionRequest transactionRequest = new TransactionRequest()
                .amount(request.getTotalAmount())
                .paymentMethodNonce(request.getNonce())
                .billingAddress()  // <-- (OPTIONAL, see official docs)
                .firstName(user.getFullname())
                .lastName(user.getEmail())
                .done()
                .options()
                .submitForSettlement(true)
                .done();

        com.braintreegateway.Result<Transaction> result = gateway.transaction().sale(transactionRequest);
        final String transactionId = getTransactionId(result);

        //OK, ALL IS GOOD! let's write to database.
        MyCustomResponse response = checkoutService.processCheckoutDatabase(transactionId, request, user);
        return ResponseEntity.ok().body(response);
    }


    private static String getTransactionId(Result<Transaction> result) {
        String transactionId; // from Braintree
        // listen for Transaction Result
        if (result.isSuccess()) {
            transactionId = result.getTarget().getId();
        } else if (result.getTransaction() != null) {
            transactionId = result.getTransaction().getId();
        } else {
            //☹ Oops! FAILED, so return errors
            ArrayList<String> errorList = new ArrayList<>();
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorList.add(error.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorList.toString());
        }
        return transactionId;
    }

}
