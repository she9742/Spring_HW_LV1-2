package com.sparta.blog.entity;


import com.sparta.blog.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //vs AUTO 처음에 오토로 돼 있었는데 첫 번째 댓글인데 아이디 값이 2로 뜸 큰 문제는 아니지만 찝찝
                                                         //아이덴티티로 바꾸니까 해결
    private Long id;


    @Column(nullable = false)
    private String contents;

    //조인을 해야 하나 이거도
    @ManyToOne // 누가 남긴 댓글인지 알아야 하니까 넣어야 해
    @JoinColumn(name = "USER_ID", nullable = false) //유저아이디로 연관관계 지어준다~
    private User user;

    @ManyToOne
    @JoinColumn(name = "BLOG_ID",nullable = false)
    private Blog blog;



    public Comment(CommentRequestDto requestDto, User user, Blog blog ) {
        this.contents = requestDto.getContents();
        this.blog = blog;
        this.user = user;
    }
    public void update(CommentRequestDto requestDto){
        this.contents = requestDto.getContents();
    }
}
