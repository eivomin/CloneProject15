package com.example.cloneproject15.token;

import lombok.Getter;

public class AuthToken {

    @Getter
    private final String token;

    public AuthToken(String token) {
        this.token = token;
    }
}
