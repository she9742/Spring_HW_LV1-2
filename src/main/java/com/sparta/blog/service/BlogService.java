package com.sparta.blog.service;


import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.BlogResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService{

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Transactional(readOnly = true)
    public List<BlogResponseDto> getAll() {
        List<Blog> blogs =  blogRepository.findAllByOrderByModifiedAtDesc(); //
        List<BlogResponseDto> blogResponseDtoList = new ArrayList<>();

        for( Blog blog : blogs ) {
            blogResponseDtoList.add(new BlogResponseDto(blog));
        }
        return blogResponseDtoList;
    }

    @Transactional
    public BlogResponseDto createBlog(BlogRequestDto requestDto,HttpServletRequest request){ //여기도 http~ 해야 하나


        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow( //로그인한 사람의 유저정보 담고 있다!
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")); //유저아이디 정보도 담아야 해서
            Blog blog = new Blog(requestDto, user);
            blogRepository.save(blog); //디비에 저장!
            return new BlogResponseDto(blog);
        }
        return null;
    }

    @Transactional
    public BlogResponseDto getBlog (Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾으시는 게시글이 없습니다.")
        );
        return new BlogResponseDto(blog);
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, BlogRequestDto requestDto, HttpServletRequest request){
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 게시글이 없습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( //로그인한 사람의 유저정보 담고 있다!
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")

            );
            //글 쓴 사람이랑 로그인한 사람 정보 같은지 비교
            //글 쓴 사람정보 blog.get ~면 은 if
            //같지 않다면 권한이 없다 너는 같으면 그냥 넘어가서 수정
//            if (!blog.getUser().getId().equals(user.getId()) || !blog.getUser().getRole().equals(user.getRole())) {
//                throw new IllegalArgumentException("수정할 권한이 없습니다.")
            if (blog.getUser().getId().equals(user.getId()) || user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                blog.update(requestDto);//requestDto에서 받아서 업데이트 안에 넣어준거야
            }else {
                throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
            }
        }
        return new BlogResponseDto(blog);
    }
    @Transactional
    public ResponseEntity deleteBlog(Long id, HttpServletRequest request){
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제할 게시글이 없습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    ()-> new IllegalArgumentException("삭제할 게글이 존재하지 않습니다.")
            );
            if (blog.getUser().getId().equals(user.getId()) || user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                blogRepository.deleteById(id);
            }else{
                throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
            }
        }
        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }
}