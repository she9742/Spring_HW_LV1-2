package com.sparta.blog.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginResponseDto {
    private String msg;
    private HttpStatus httpStatus;

    public LoginResponseDto(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}
