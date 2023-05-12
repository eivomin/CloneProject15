package com.example.cloneproject15.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiResponse {

    public ApiResponse(String name, String token) {
    }

    public static ApiResponse success(String name, String token) {
        return new ApiResponse(name, token);
    }
}
