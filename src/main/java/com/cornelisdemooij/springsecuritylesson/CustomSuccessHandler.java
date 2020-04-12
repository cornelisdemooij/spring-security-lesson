package com.cornelisdemooij.springsecuritylesson;

import com.cornelisdemooij.springsecuritylesson.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String jwt = jwtUtil.generateToken(userDetails);
        response.addHeader(
            HttpHeaders.SET_COOKIE,
            "Authorization=".concat(jwt).concat("; HttpOnly")
        );
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
