package com.sparta.blog.controller;


import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/api/comments")
    public Comment createComment(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @PutMapping("/api/comments/{id}")
    public String updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    @DeleteMapping("/api/comments/{id}")
    public String deleteComment(@PathVariable Long id,HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }
}
