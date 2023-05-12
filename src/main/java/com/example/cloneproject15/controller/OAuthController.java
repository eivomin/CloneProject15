package com.example.cloneproject15.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Tag(name = "OAuthController", description = "OAuth Controller")
public class OAuthController {

    @Operation(summary = "카카오callback API" , description = "카카오 callback")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "회원 가입 완료" )})
    @GetMapping("/oauth/kakao")
    public void kakaoCallback(@RequestParam String code){
        System.out.println(code);
    }
}
