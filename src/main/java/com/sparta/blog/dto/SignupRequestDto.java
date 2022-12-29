package com.sparta.blog.dto;

import com.sparta.blog.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Setter
@Getter
public class SignupRequestDto {

    @Size(min= 4, max= 10)
    @Pattern(regexp="^[a-z0-9]*$")
    private String username;


    @Size(min= 8, max= 15)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$)")
    private String password;

//    private boolean admin = false;
    private String adminToken = "";

    private UserRoleEnum userRoleEnum;


}
