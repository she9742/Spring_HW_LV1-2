package com.sparta.blog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class SignupResponseDto{


    private String msg;
    private HttpStatus httpStatus;


    public SignupResponseDto(String msg, HttpStatus httpStatus) {
        this.msg = msg;
        this.httpStatus = httpStatus;
    }
}
