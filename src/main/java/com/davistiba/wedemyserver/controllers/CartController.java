package com.davistiba.wedemyserver.controllers;

import com.davistiba.wedemyserver.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured("ROLE_USER")
@RequestMapping(path = "/cart")
public class CartController {

    @Autowired
    private CartRepository cartRepository;


}
