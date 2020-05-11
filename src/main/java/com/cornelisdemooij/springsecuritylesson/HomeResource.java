package com.cornelisdemooij.springsecuritylesson;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeResource {
    private String links = "<br><a href='/'>Home</a> " +
            "<a href='/logout'>Logout</a>";

    @GetMapping("/")
    public String user() {
        return ("<h1>Welkom gebruiker!</h1>" +
                "Deze pagina hoort alleen zichtbaar te zijn voor ingelogde gebruikers." + links);
    }
}
