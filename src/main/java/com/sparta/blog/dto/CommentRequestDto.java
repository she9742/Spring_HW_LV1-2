package com.sparta.blog.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

    private String title;
    private String username;
    private String contents;

}
