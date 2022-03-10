package com.davistiba.wedemyserver.controllers;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Transaction;
import com.davistiba.wedemyserver.config.BraintreeConfig;
import com.davistiba.wedemyserver.dto.OrderItemDTO;
import com.davistiba.wedemyserver.dto.ReceiptDTO;
import com.davistiba.wedemyserver.dto.SalesDTO;
import com.davistiba.wedemyserver.models.User;
import com.davistiba.wedemyserver.repository.OrderItemRepository;
import com.davistiba.wedemyserver.repository.SalesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/sales")
public class SalesController {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private final BraintreeGateway gateway = BraintreeConfig.getGateway();

    private static final Logger logger = LoggerFactory.getLogger(SalesController.class);


    @GetMapping(path = "/mine")
    public List<SalesDTO> getAllMyOwnedItems(@NotNull HttpSession session, @RequestParam(defaultValue = "0") Integer page) {
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        return salesRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, 10));
    }

    @GetMapping(path = "/mine/{transactionId}")
    public List<OrderItemDTO> getItemsbyTransactionId(@PathVariable String transactionId,
                                                      @RequestParam(defaultValue = "0") Integer page) {
        return orderItemRepository.findByTransactionIdEquals(transactionId, PageRequest.of(page, 10));
    }


    // Get ALL transaction details (seller, buyer, method, etc.) by ID for receipt
    @GetMapping(path = "/details/{transactionId}")
    public ResponseEntity<ReceiptDTO> getReceiptByTransactionId(@PathVariable @NotBlank String transactionId,
                                                                @NotNull HttpSession session) {
        try {
            long start = System.currentTimeMillis();
            Transaction t = gateway.transaction().find(transactionId); // CALL BRAINTREE API.
            List<OrderItemDTO> orderItems = orderItemRepository.findByTransactionIdEquals(transactionId, Pageable.unpaged());

            User customer = AuthController.getSessionUserDetails(session); // get from Redis Session store

            //construct result payload
            ReceiptDTO receiptDTO = new ReceiptDTO(
                    customer.getFullname(),
                    customer.getEmail(),
                    t.getCreatedAt().toInstant(),
                    transactionId,
                    "Wedemy Inc.",
                    t.getAmount(),
                    t.getStatus().name(),
                    t.getCurrencyIsoCode(),
                    t.getPaymentInstrumentType().toUpperCase(),
                    UUID.randomUUID().toString(),
                    orderItems
            );

            logger.info("time taken: {} ms", (System.currentTimeMillis() - start));
            return ResponseEntity.ok().body(receiptDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find transaction", e);
        }
    }


}
