package com.example.cloneproject15.exception;

import com.example.cloneproject15.config.SentrySupport;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private ExceptionEnum error;
    private SentrySupport sentrySupport = new SentrySupport();

    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
        sentrySupport.logSimpleMessage(error.getMessage());
    }
}
