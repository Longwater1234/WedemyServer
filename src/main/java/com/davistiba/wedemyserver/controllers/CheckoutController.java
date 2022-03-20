package com.davistiba.wedemyserver.controllers;

import com.braintreegateway.*;
import com.davistiba.wedemyserver.config.BraintreeConfig;
import com.davistiba.wedemyserver.dto.CheckoutRequest;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.service.CheckoutService;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
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

    @Autowired
    public CheckoutController(BraintreeGateway gateway, CheckoutService checkoutService) {
        this.gateway = BraintreeConfig.getGateway();
        this.checkoutService = checkoutService;
    }

    @GetMapping(path = "/token")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> getClientToken() {
        Map<String, String> response = new HashMap<>();
        String clientToken = gateway.clientToken().generate();
        response.put("clientToken", clientToken);
        return response;
    }


    @PostMapping(path = "/complete")
    public ResponseEntity<Map<String, Object>> completePurchase(@Valid @RequestBody CheckoutRequest request,
                                                                @NotNull HttpSession session) {
        String transactionId;
        Map<String, Object> response;
        User user = MyUserDetailsService.getSessionUserDetails(session); //from redis Store

        // try to create Braintree transaction
        TransactionRequest transactionRequest = new TransactionRequest()
                .amount(request.getTotalAmount())
                .paymentMethodNonce(request.getNonce())
                .billingAddress()  // <-- user details (OPTIONAL)
                .firstName(user.getFullname())
                .lastName(user.getEmail())
                .done()
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);

        // listen for Braintree Transaction Result
        if (result.isSuccess()) {
            transactionId = result.getTarget().getId();
        } else if (result.getTransaction() != null) {
            transactionId = result.getTransaction().getId();
        } else {
            //Oops! ☹
            List<String> errorList = new ArrayList<>();
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                errorList.add(error.getMessage());
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorList.toString());
        }

        //OK, ALL IS GOOD! let's finally wrap everything up.
        try {
            response = checkoutService.processCheckoutDatabase(transactionId, request, user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not complete purchase", e);
        }
    }


}
