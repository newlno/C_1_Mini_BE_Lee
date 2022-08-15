package com.example.C_1_Mini_BE.Model;

import com.example.C_1_Mini_BE.Dto.Request.PostRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder  // 빌더패턴 사용하기 위한 어노테이션
@Getter  // 멤버변수 값 호출
@Setter  // 맴버변수 값 적용
@NoArgsConstructor  // 기본 생성자를 생성
@AllArgsConstructor //전체 변수를 생성하는 생성자
@Entity  // JPA를 사용하여 DB와 매핑
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imgUrl;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;

    // 수정
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.imgUrl = postRequestDto.getImgUrl();
    }

    // 작성자 확인하기
    public boolean validateUser(User user) {
        return !this.user.equals(user);
    }
}
