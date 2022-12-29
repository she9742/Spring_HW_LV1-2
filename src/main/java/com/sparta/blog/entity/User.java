package com.sparta.blog.entity;


import com.sparta.blog.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) //
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRoleEnum;


//    @OneToMany
//    List<Blog> blogList = new ArrayList<>(); // 외래키!! 외래키는 블로그에서 유저아이디를 외래키로 가지고 있어
    //단방향으로 가능하면 단방향으로 하는 것이 좋다 그리고 ManyToOne이 우선이다? -> 그러면 얘는 없어도 되는 건가?!
    //근데 필요한 거 아녀? 왜 ??? 여기도 정보를 받아 와야지..? 엥 무슨 소리야?


    public User(SignupRequestDto requestDto){ //회원가입 때 받은 정보를 디티오에 담아서 넘겨준다!
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.userRoleEnum = requestDto.getUserRoleEnum();
    }

//    public User(String username, String password, UserRoleEnum userRoleEnum) {
//    }
//
//    public User(String username, String password ) {
//        this.username = username;
//        this.password = password;
//
//    }

//    public User(String username, String password, UserRoleEnum userRoleEnum) {
//        this.username = username;
//        this.password = password;
//        this.userRoleEnum = userRoleEnum;
//    }
}
