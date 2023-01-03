package com.sparta.blog.service;


import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
//        String password = signupRequestDto.getPassword();


        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

//        UserRoleEnum userRoleEnum = UserRoleEnum.USER;
//        if (signupRequestDto.isAdmin()) {
//            if (!(ADMIN_TOKEN).equals(signupRequestDto.getAdminToken())){
//                throw new IllegalArgumentException("관리자 암호가 달라서 등록이 불가능합니다.");
//            }
//            userRoleEnum = UserRoleEnum.ADMIN;
//        }
        if (signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) { //회원가입 dto에 저장된 토큰이랑
            signupRequestDto.setUserRoleEnum(UserRoleEnum.ADMIN);//admin 권한 넣어줘 디티오에

        } else if (signupRequestDto.getAdminToken().equals("")) {
            signupRequestDto.setUserRoleEnum(UserRoleEnum.USER);

        } else {
            throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
        }

        User user = new User(signupRequestDto);
        userRepository.save(user);
        return "success";
    }

    @Transactional(readOnly = true)
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getUserRoleEnum()));
        return "로그인 성공!";
    }
}

