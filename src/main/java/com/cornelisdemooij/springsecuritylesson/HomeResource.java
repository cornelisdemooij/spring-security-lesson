package com.cornelisdemooij.springsecuritylesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home() {
        return ("<h1>Helloooooooo Young Colfield</h1>" +
                "<a href='/logout'>Logout</a>");
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>" +
                "<a href='/logout'>Logout</a>");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>" +
                "<a href='/logout'>Logout</a>");
    }
}
