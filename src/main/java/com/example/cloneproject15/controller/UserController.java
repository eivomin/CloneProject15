package com.example.cloneproject15.controller;

import com.example.cloneproject15.dto.StatusResponseDto;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserController", description = "유저 관련 Controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "유저 가입 API" , description = "새로운 유저 가입")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "회원 가입 완료" )})
    @PostMapping("/signup")
    public StatusResponseDto signup(UserRequestDto requestDto,
                                    @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        return userService.signup(requestDto, image);
    }

    @Operation(summary = "유저 로그인 API" , description = "로그인, RefreshToken, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "로그인 성공!" )})
    @PostMapping("/login")
    public StatusResponseDto login(@RequestBody UserRequestDto requestDto, HttpServletResponse response){
        return userService.login(requestDto, response);
    }

    @Operation(summary = "유저 로그아웃 API" , description = "로그아웃, RefreshToken, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "로그아웃 성공!" )})
    @PostMapping("/logout")
    public StatusResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
        return userService.logout(userDetails.getUser(), request);
    }

    @Operation(summary = "유저 정보조회 API" , description = "유저정보조회, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "유저정보 조회 반환 성공!" )})
    @GetMapping("/user-info")
    public UserResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String userid = userDetails.getUsername();
        return userService.findUserInfo(userid);
    }

    @Operation(summary = "유저 목록 API" , description = "유저목록조회")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "유저목록 조회 반환 성공!" )})
    @GetMapping("/users")
    public List<UserResponseDto> getUsers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getUsers();
    }

    @Operation(summary = "특정 유저 정보조회 API" , description = "유저정보조회, AccessToken")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "유저정보 조회 반환 성공!" )})
    @GetMapping("/user-info/{userId}")
    public UserResponseDto getUserInfo2(@PathVariable String userId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.findUserInfo(userId);
    }
}
