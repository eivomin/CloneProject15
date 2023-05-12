package com.example.cloneproject15.controller;

import com.example.cloneproject15.dto.UserRequestDto;
import com.example.cloneproject15.dto.UserResponseDto;
import com.example.cloneproject15.security.UserDetailsImpl;
import com.example.cloneproject15.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserController", description = "유저 관련 Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 가입 API" , description = "새로운 유저 가입")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "회원 가입 완료" )})
    @PostMapping("/signup")
    public UserResponseDto signup(@RequestBody UserRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @Operation(summary = "유저 로그인 API" , description = "로그인, RefreshToken, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "로그인 성공!" )})
    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }

    @Operation(summary = "유저 로그아웃 API" , description = "로그아웃, RefreshToken, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "로그아웃 성공!" )})
    @PostMapping("/logout")
    public UserResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        return userService.logout(userDetails.getUser(), request);
    }
}
