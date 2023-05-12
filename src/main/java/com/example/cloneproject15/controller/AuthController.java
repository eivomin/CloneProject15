package com.example.cloneproject15.controller;

import com.example.cloneproject15.common.ApiResponse;
import com.example.cloneproject15.entity.AuthReqModel;
import com.example.cloneproject15.token.AuthToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/login")
    public ApiResponse login(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody AuthReqModel authReqModel){

        AuthToken accessToken = new AuthToken("token");
        return ApiResponse.success("token", "tfefefefefefefe");
    }

    @GetMapping("/refresh")
    public ApiResponse refreshToken(HttpServletRequest request,
                                    HttpServletResponse response){
        AuthToken newAccessToken = new AuthToken("token");
        return ApiResponse.success("token", "tfefefefefefefe");
    }
}
