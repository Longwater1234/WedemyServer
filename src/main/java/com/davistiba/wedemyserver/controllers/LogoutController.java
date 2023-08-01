package com.davistiba.wedemyserver.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @SecurityRequirement(name = "cookieAuth")
    @SecurityRequirement(name = "sessionKey")
    @PostMapping("/logout")
    public void logout() {
    }
}