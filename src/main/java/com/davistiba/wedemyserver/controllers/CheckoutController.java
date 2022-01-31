package com.davistiba.wedemyserver.controllers;

import com.braintreegateway.BraintreeGateway;
import com.davistiba.wedemyserver.config.BraintreeConfig;
import com.davistiba.wedemyserver.models.CheckoutRequest;
import com.davistiba.wedemyserver.models.MyCustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/checkout")
public class CheckoutController {

    private final BraintreeGateway gateway;

    public CheckoutController(BraintreeGateway gateway) {
        this.gateway = BraintreeConfig.getGateway();
    }

    @GetMapping(path = "/token")
    @ResponseStatus(value = HttpStatus.OK)
    public Map<String, String> getClientToken() {
        try {
            Map<String, String> response = new HashMap<>();
            String clientToken = gateway.clientToken().generate();
            response.put("clientToken", clientToken);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping(path = "/complete")
    public ResponseEntity<MyCustomResponse> completePurchase(@Valid @RequestBody CheckoutRequest request,
                                                             @NotNull HttpSession session) {

        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        //todo COMPLETE THE METHOD, add try/catch
        return ResponseEntity.ok().body(new MyCustomResponse("Successfully purchased courseId " + request.getCourseId()));
    }


}
