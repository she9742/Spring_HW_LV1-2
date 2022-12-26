package com.sparta.blog.service;


import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.User;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService{

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    @Transactional(readOnly = true)
    public List<Blog> getBlogAll(){
        return blogRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional
    public Blog createBlog(BlogRequestDto requestDto,HttpServletRequest request){ //여기도 http~ 해야 하나


        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null){
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else{
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( //로그인한 사람의 유저정보 담고 있다!
                    ()-> new IllegalArgumentException("사용자가 존재하지 않습니다.")); //유저아이디 정보도 담아야 해서
            Blog blog = new Blog(requestDto, user);
            return blogRepository.save(blog); //디비에 저장!
        }
        return null;
    }

    @Transactional
    public Optional<Blog>findBlogById(Long id){
        return Optional.ofNullable(blogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("찾으시는 글이 없습니다.")
        ));
    }

    @Transactional
    public String updateBlog(Long id, BlogRequestDto requestDto, HttpServletRequest request){
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 글이 없습니다.")
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
            if (!blog.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("수정할 권한이 없습니다.");
            }
        }
        blog.update(requestDto);//requestDto에서 받아서 업데이트 안에 넣어준거야
        return blog.getContents();
    }
    @Transactional
    public String deleteBlog(Long id, BlogRequestDto requestDto, HttpServletRequest request){
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제할 글이 없습니다.")
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
                    ()-> new IllegalArgumentException("삭제할 글이 존재하지 않습니다.")
            );
            if (!blog.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("삭제할 권한이 없습니다.");
            }
        }
        blogRepository.deleteById(id);
        return "삭제되었습니다.";
    }
}