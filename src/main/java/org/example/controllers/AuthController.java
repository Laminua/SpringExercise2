package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        log.info("Authentication page has been requested");

        return "auth/login";
    }

    @GetMapping("/badLogin")
    public String showBadLoginPage() {
        log.info("Incorrect credentials message page has been requested");

        return "auth/bad-login";
    }
}
