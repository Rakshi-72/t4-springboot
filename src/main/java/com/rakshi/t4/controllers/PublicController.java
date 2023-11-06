package com.rakshi.t4.controllers;

import com.rakshi.t4.config.JwtUtil;
import com.rakshi.t4.models.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/")
@RequiredArgsConstructor
public class PublicController {

    private final AuthenticationManager manager;
    private final JwtUtil util;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        System.out.println(login);
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = util.generateToken(login.getUsername());
        return ResponseEntity.ok(token);
    }

}
