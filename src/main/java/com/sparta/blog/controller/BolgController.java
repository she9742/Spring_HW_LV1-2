package com.sparta.blog.controller;

import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.service.BlogService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BolgController {
    
    private final BlogService blogService;

    @GetMapping("/api/blogs") //전체 게시글 조회
    public List<Blog> getBlogList(){
        return blogService.getBlogAll();
    }

    @PostMapping("/api/blogs") //작성
    public Blog createMessage(@RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.createBlog(requestDto, request);
    }

    @GetMapping("/api/blogs/{id}") //선택한 게시글 조회
    public Optional<Blog> findBlog(@PathVariable Long id) {
        return blogService.findBlogById(id);
    }

    @PutMapping("/api/blogs/{id}") //수정
    public String updateBlog(@PathVariable Long id, @RequestBody BlogRequestDto requestDto, HttpServletRequest request) {
        return blogService.updateBlog(id, requestDto, request);
    }

    @DeleteMapping("/api/blogs/{id}") //삭제
    public String deleteBlog(@PathVariable Long id,HttpServletRequest request) {
        return blogService.deleteBlog(id, request);
    }

}