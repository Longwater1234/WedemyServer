package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.models.Sales;
import com.davistiba.wedemyserver.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/sales")
public class SalesController {

    @Autowired
    SalesRepository salesRepository;

    @GetMapping(path = "/mine")
    public List<Sales> getAllMyOwnedItems(@NotNull HttpSession session) {
        Integer userId = (Integer) session.getAttribute(AuthController.USERID);
        return salesRepository.findByUserId_IdEquals(userId);
    }

}
