package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BolgController  {


    private final BlogService blogService;

    @GetMapping("/blogs/lists")
    public ResponseEntity<List<BlogResponseDto>> getAll(){
        List<BlogResponseDto> blogResponseDtos = blogService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDtos);
    }

    @PostMapping("/blogs")
    public ResponseEntity<BlogResponseDto> createBlog(@RequestBody BlogRequestDto blogDto, HttpServletRequest request){
        BlogResponseDto blogResponseDto = blogService.createBlog(blogDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);
    }


    @GetMapping("/blogs/{id}")
    public ResponseEntity<BlogResponseDto> getBlog(@PathVariable Long id){
        BlogResponseDto blogResponseDto = blogService.getBlog(id);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);

//        return new ResponseEntity<List<BoardResponseDto>>(board);
    }

    @PutMapping("/blogs/{id}")
    public ResponseEntity<BlogResponseDto> updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto blogDto , HttpServletRequest request){
        BlogResponseDto blogResponseDto = blogService.updateBlog(id,blogDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(blogResponseDto);
    }


    @DeleteMapping("/blogs/{id}")
    public ResponseEntity deleteBlog(@PathVariable Long id, HttpServletRequest request){
        return blogService.deleteBlog(id,request);
    }
}

