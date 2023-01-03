package com.sparta.blog.service;



import com.sparta.blog.dto.CommentRequestDto;

import com.sparta.blog.dto.CommentResponseDto;
import com.sparta.blog.entity.Blog;
import com.sparta.blog.entity.Comment;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.BlogRepository;
import com.sparta.blog.repository.CommentRepository;
import com.sparta.blog.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public CommentResponseDto createComment(Long id,CommentRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        Blog blog = blogRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시글을 찾을 수 없습니다.")
        );

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            Comment comment = new Comment(requestDto, user,blog);
            commentRepository.save(comment);
            return new CommentResponseDto(comment);
        }
        return null;
    }

    @Transactional
    public CommentResponseDto updateComment( CommentRequestDto requestDto, HttpServletRequest request,Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
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
        return new CommentResponseDto(comment);
    }

    @Transactional
    public ResponseEntity deleteComment(HttpServletRequest request, Long commentId){

        Comment comment = commentRepository.findById(commentId).orElseThrow(
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
                commentRepository.deleteById(commentId);
            }else{
                throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
            }
        }

        return new ResponseEntity<>("삭제 성공!", HttpStatus.OK);
    }
}






