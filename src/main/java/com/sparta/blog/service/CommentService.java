package com.sparta.blog.service;


import com.sparta.blog.dto.BlogRequestDto;
import com.sparta.blog.dto.CommentRequestDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Comment createComment(CommentRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Comment comment = new Comment(requestDto, user);
            return commentRepository.save(comment);
        }
        return null;
    }

    @Transactional
    public String updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("수정하려는 댓글이 없습니다.")
        );

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")

            );
            if (comment.getUser().getId().equals(user.getId()) || user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                comment.update(requestDto);
            }else{
                throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
            }
        }
        return comment.getContents();
    }

    @Transactional
    public String deleteComment(Long id, HttpServletRequest request){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("삭제할 댓글이 없습니다.")
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
                    ()-> new IllegalArgumentException("삭제할 댓글이 존재하지 않습니다.")
            );
            if (comment.getUser().getId().equals(user.getId()) || user.getUserRoleEnum().equals(UserRoleEnum.ADMIN)) {
                commentRepository.deleteById(id);
            }else{
                throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
            }
        }

        return "삭제되었습니다.";
    }
}






