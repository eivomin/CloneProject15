package com.example.cloneproject15.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "set")
public class ResponseDto<T> {
    private StatusCode status;
    private String message;
    private T data;

    public static <T> ResponseDto<T> setSuccess(String message, T data){
        return ResponseDto.set(StatusCode.OK, message, data);
    }

    public static <T> ResponseDto<T> setBadRequest(String message){
        return ResponseDto.set(StatusCode.BAD_REQUEST, message, null);
    }
}

