package com.sparta.blog.dto;

import com.sparta.blog.entity.Comment;

import java.time.LocalDateTime;

public class CommentResponseDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContents();
        this.createdAt =comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
