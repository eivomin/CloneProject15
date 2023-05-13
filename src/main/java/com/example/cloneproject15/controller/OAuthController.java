package com.example.cloneproject15.controller;

import com.example.cloneproject15.jwt.JwtUtil;
import com.example.cloneproject15.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@AllArgsConstructor
public class OAuthController {

    private final KakaoService kakaoService;

    @GetMapping("/board")
    public ModelAndView board() {
        return new ModelAndView("index");
    }

    @GetMapping("/oauth/kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        System.out.println("컨트롤러 진입");
        return kakaoService.kakaoLogin(code, response);

    }
}
