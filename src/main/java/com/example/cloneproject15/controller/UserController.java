package com.example.cloneproject15.controller;

import com.example.cloneproject15.common.ApiResponse;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-info")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
    public ApiResponse getUserInfo(){
        //User user = new User();
        return ApiResponse.success("user", "user객체 반환 예정");
    }
}
