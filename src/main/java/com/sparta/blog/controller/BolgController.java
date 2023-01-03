package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.service.BlogService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BolgController {

    private final BlogService blogService;

    @GetMapping("/blogs") //전체 게시글 조회
    public ResponseEntity<List<BlogResponseDto>> getAll(){
        List<BlogResponseDto> blogResponseDtos = blogService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDtos);
    }

    @PostMapping("/blogs") //작성
    public ResponseEntity<BlogResponseDto> createBlog(@RequestBody BlogRequestDto blogDto, HttpServletRequest request){
        BlogResponseDto blogResponseDto = blogService.createBlog(blogDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);
    }

    @GetMapping("/blogs/{id}") //선택한 게시글 조회
    public ResponseEntity<BlogResponseDto> getBoard(@PathVariable Long id){
        BlogResponseDto blogResponseDto = blogService.getBlog(id);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);
    }

    @PutMapping("/blogs/{id}") //수정
    public ResponseEntity<BlogResponseDto> updateBoard(@PathVariable Long id, @RequestBody BlogRequestDto blogDto , HttpServletRequest request){
        BlogResponseDto blogResponseDto = blogService.updateBlog(id,blogDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);
    }


    @DeleteMapping("/blogs/{id}")//삭제
    public ResponseEntity deleteBlog(@PathVariable Long id, HttpServletRequest request){
        return blogService.deleteBlog(id,request);
    }

}