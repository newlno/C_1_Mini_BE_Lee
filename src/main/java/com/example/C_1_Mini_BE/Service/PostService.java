package com.example.C_1_Mini_BE.Service;


import com.example.C_1_Mini_BE.Dto.Request.PostRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.AllPostResponseDto;
import com.example.C_1_Mini_BE.Dto.Response.CommentResponseDto;
import com.example.C_1_Mini_BE.Dto.Response.PostResponseDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Jwt.TokenProvider;
import com.example.C_1_Mini_BE.Model.Comment;
import com.example.C_1_Mini_BE.Model.Post;
import com.example.C_1_Mini_BE.Model.User;
import com.example.C_1_Mini_BE.Model.UserDetailsImpl;
import com.example.C_1_Mini_BE.Repository.CommentRepository;
import com.example.C_1_Mini_BE.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@Service  // 비즈니스 로직을 담은 클래스를 빈으로 등록시키기 위해 사용
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;




    // 게시글 생성
    @Transactional  // 선언적 트랜잭션, 중간에 에러나면 없던 일로 처리해줌
    public ResponseDto<?> createPost(PostRequestDto requestDto, UserDetailsImpl userDetailsImpl){
        User user = userDetailsImpl.getUser();
        Post post = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imgUrl(requestDto.getImgUrl())
                .user(user)
                .build();
        postRepository.save(post);
        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .username(userDetailsImpl.getUsername())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 게시글 개별조회
    @Transactional(readOnly = true)
    public ResponseDto<?> getPost(Long id) {
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .username(comment.getUser().getUsername())
                            .content(comment.getContent())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        return ResponseDto.success(
                PostResponseDto.builder()
                        .id(post.getId())
                        .username(post.getUser().getUsername())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .imgUrl(post.getImgUrl())
                        .comments(commentResponseDtoList)
                        .createdAt(post.getCreatedAt())
                        .modifiedAt(post.getModifiedAt())
                        .build()
        );
    }

    // 게시글 전체조회
    public ResponseDto<?> getAllPost() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        List<AllPostResponseDto> allPostResponseDtoList = new ArrayList<>();
        for (Post post : postList) {
            int comments = commentRepository.countAllByPost(post);
            AllPostResponseDto allPostResponseDto = AllPostResponseDto.builder()
                    .id(post.getId())
                    .username(post.getUser().getUsername())
                    .title(post.getTitle())
                    .imgUrl(post.getImgUrl())
                    .commentsNum(comments)
                    .build();
            allPostResponseDtoList.add(allPostResponseDto);
        }
        return ResponseDto.success(allPostResponseDtoList);
    }

    // 게시글 수정
    @Transactional
    public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }
        if (post.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }
        post.update(requestDto);
        return ResponseDto.success(PostResponseDto.builder()
                .id(post.getId())
                .username(user.getUsername())
                .content(post.getContent())
                .imgUrl(post.getImgUrl())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build()
        );
    }

    // 게시글 삭제
    @Transactional
    public ResponseDto<?> deletePost(Long id, UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        Post post = isPresentPost(id);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }
        if (post.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
        return ResponseDto.success("post delete success");
    }

    // 게시글 찾기
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }

    // 이중검사한거임 <- 순서 확인
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
