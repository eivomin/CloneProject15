package com.example.cloneproject15.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HelloMessage {

    private String message;

    private String roomId;

    public HelloMessage(String message, String roomId) {
        this.roomId = roomId;
        this.message = message;
    }
}