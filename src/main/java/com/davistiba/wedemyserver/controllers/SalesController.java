package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.dto.OrderItemDTO;
import com.davistiba.wedemyserver.dto.SalesDTO;
import com.davistiba.wedemyserver.repository.OrderItemRepository;
import com.davistiba.wedemyserver.repository.SalesRepository;
import com.davistiba.wedemyserver.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/sales")
public class SalesController {

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @GetMapping(path = "/mine")
    public Slice<SalesDTO> getAllMyOwnedItems(@NotNull HttpSession session, @RequestParam(defaultValue = "0") Integer page) {
        Integer userId = (Integer) session.getAttribute(MyUserDetailsService.USERID);
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "createdAt");
        //TODO fix frontend
        return salesRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @GetMapping(path = "/mine/{transactionId}")
    public Slice<OrderItemDTO> getItemsbyTransactionId(@PathVariable String transactionId,
                                                      @RequestParam(defaultValue = "0") Integer page) {
        //TODO FIX FRONTEND
        return orderItemRepository.findByTransactionIdEquals(transactionId, PageRequest.of(page, 10));
    }


}
