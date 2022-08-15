package com.example.C_1_Mini_BE.Model;


import com.example.C_1_Mini_BE.Dto.Request.CommentRequestDto;
import lombok.*;

import javax.persistence.*;

@Builder  // 빌더패턴 사용하기 위한 어노테이션
@Getter  // 멤버변수 값 호출
@NoArgsConstructor  // 기본 생성자를 생성
@AllArgsConstructor //전체 변수를 생성하는 생성자
@Entity  // JPA를 사용하여 DB와 매핑
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Column(nullable = false)
    private String content;

    // 수정
    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
    public boolean validateUser(User user) {
        return !this.user.equals(user);
    }
}
