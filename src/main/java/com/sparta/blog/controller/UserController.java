package com.sparta.blog.controller;


import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.LoginResponseDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.dto.SignupResponseDto;
import com.sparta.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user") //이게 기본 url
public class UserController {

    private  final UserService userService;

//    @GetMapping("/signup")
//    public ModelAndView signupPage() {
//        return new ModelAndView("signup");
//    }
//
//    @GetMapping("/login")
//    public  ModelAndView loginPage(){
//        return new ModelAndView("login");
//    }
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        String msg = userService.signup(requestDto);
        SignupResponseDto responseDto = new SignupResponseDto(msg, HttpStatus.OK);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        String msg = userService.login(requestDto, response);
        LoginResponseDto responseDto = new LoginResponseDto(msg, HttpStatus.OK);

        //userService.login(loginRequestDto,response);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}


