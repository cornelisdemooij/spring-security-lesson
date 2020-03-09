package com.cornelisdemooij.springsecuritylesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home() {
        return ("<h1>Helloooooooo Young Colfield</h1>");
    }
}
