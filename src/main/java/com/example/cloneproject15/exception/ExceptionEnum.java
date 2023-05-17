package com.example.cloneproject15.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "아이디 또는 비밀번호가 일치하지 않습니다."),
    PASSWAORD_REGEX(HttpStatus.BAD_REQUEST, "400", "비밀번호는 알파벳 대소문자 하나 이상 숫자중 하나 이상 6자리 이상 조합으로 구성되어야 합니다."),
    MAX_FILE_SIZE(HttpStatus.BAD_REQUEST, "400", "첨부 가능한 파일 크기는 20MB 미만입니다."),
    UNAUTHORIZED_FILE(HttpStatus.BAD_REQUEST, "400", "첨부 가능한 파일 확장자(.png, .jpeg, .jpg, .webp, .gif)만 가능합니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "권한이 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404_1", "회원이 존재하지 않습니다."),
    DUPLICATED_USER_ID(HttpStatus.CONFLICT, "409", "중복된 아이디가 이미 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}