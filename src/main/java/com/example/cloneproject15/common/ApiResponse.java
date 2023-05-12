package com.example.cloneproject15.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final ApiResponseHeader header;
    private final Map<String, T> body;

    public static <T> ApiResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);
        return new ApiResponse(new ApiResponseHeader("success", "성공 메시지"), map);
    }
}
