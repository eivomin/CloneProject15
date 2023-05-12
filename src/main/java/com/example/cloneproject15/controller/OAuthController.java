package com.example.cloneproject15.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OAuthController {

    @GetMapping("/oauth/kakao")
    public void kakaoCallback(@RequestParam String code){
        System.out.println(code);
    }
}
