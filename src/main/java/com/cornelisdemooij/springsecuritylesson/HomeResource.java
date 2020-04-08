package com.cornelisdemooij.springsecuritylesson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    private String links = "<a href='/'>Home</a> " +
            "<a href='/user'>User</a> " +
            "<a href='/admin'>Admin</a> " +
            "<a href='/logout'>Logout</a>";

    @GetMapping("/")
    public String home() {
        return ("<h1>Helloooooooo Young Colfield</h1>" + links);
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>" + links);
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>" + links);
    }
}
