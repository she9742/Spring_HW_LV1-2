package com.sparta.blog.controller;


import com.sparta.blog.dto.CommentRequestDto;

import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/boards/{id}/comments")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){
        CommentResponseDto commentResponseDto = commentService.createComment(id,requestDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment( @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request, @PathVariable Long commentId ){
        CommentResponseDto commentResponseDto = commentService.updateComment(commentRequestDto,request,commentId);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(HttpServletRequest request, @PathVariable Long commentId){
        return commentService.deleteComment(request,commentId);
    }
}
