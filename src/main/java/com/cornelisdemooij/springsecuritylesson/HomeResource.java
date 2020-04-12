package com.cornelisdemooij.springsecuritylesson;

import com.cornelisdemooij.springsecuritylesson.models.AuthenticationRequest;
import com.cornelisdemooij.springsecuritylesson.models.AuthenticationResponse;
import com.cornelisdemooij.springsecuritylesson.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

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

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
    ) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
