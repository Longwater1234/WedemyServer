package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.OrderItemDTO;
import com.davistiba.wedemyserver.dto.SalesDTO;
import com.davistiba.wedemyserver.repository.OrderItemRepository;
import com.davistiba.wedemyserver.repository.SalesRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/sales")
public class SalesController {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @GetMapping(path = "/mine")
    public List<SalesDTO> getAllMyOwnedItems(@NotNull HttpSession session, @RequestParam(defaultValue = "0") Integer page) {
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        return salesRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, 10));
    }

    @GetMapping(path = "/mine/{transactionId}")
    public List<OrderItemDTO> getItemsbyTransactionId(@PathVariable String transactionId,
                                                      @RequestParam(defaultValue = "0") Integer page) {
        return orderItemRepository.findByTransactionIdEquals(transactionId, PageRequest.of(page, 10));
    }


}
