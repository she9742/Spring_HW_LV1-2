package com.sparta.blog.dto;

import com.sparta.blog.entity.Blog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogResponseDto {
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BlogResponseDto(Blog blog) {
        this.title = blog.getTitle();
        this.content = blog.getContents();
        this.createdAt = blog.getCreatedAt();
        this.modifiedAt = blog.getModifiedAt();
    }
}
