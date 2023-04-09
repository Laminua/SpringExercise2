package org.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class UserController {

    @RequestMapping("/welcome")
    public String showWelcomePage() {
        log.info("Welcome page for access level \"USER\" has been requested");

        return "welcome";
    }
}