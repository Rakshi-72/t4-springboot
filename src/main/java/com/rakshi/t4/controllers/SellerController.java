package com.rakshi.t4.controllers;

import com.rakshi.t4.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/seller/")
@RequiredArgsConstructor
public class SellerController {

    private final JwtUtil util;

    @GetMapping("check")
    public String sellerCheck() {
        System.out.println(util.extractCurrentUser());
        return "this is seller controller";

    }

}
