package com.sparta.blog.entity;


import com.sparta.blog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    //조인을 해야 하나 이거도
    @ManyToOne // 누가 남긴 댓글인지 알아야 하니까 넣어야 해
    @JoinColumn(name = "USER_ID", nullable = false) //유저아이디로 연관관계 지어준다~
    private User user;



    public Comment(CommentRequestDto requestDto, User user ) {
        this.title = requestDto.getTitle();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
        this.user = user;
    }
    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContents();
        this.title = requestDto.getTitle();
    }
}
