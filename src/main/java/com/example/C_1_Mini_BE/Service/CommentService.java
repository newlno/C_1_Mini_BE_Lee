package com.example.C_1_Mini_BE.Service;


import com.example.C_1_Mini_BE.Dto.Request.CommentRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.CommentResponseDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Jwt.TokenProvider;
import com.example.C_1_Mini_BE.Model.Comment;
import com.example.C_1_Mini_BE.Model.Post;
import com.example.C_1_Mini_BE.Model.User;
import com.example.C_1_Mini_BE.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@Service  // 비즈니스 로직을 담은 클래스를 빈으로 등록시키기 위해 사용
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final TokenProvider tokenProvider;

    // 댓글 생성
    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }

        Comment comment = Comment.builder()
                .user(post.getUser())
                .post(post)
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 ID 입니다.");
        }
        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
        }
        if (comment.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }
        comment.update(requestDto);
        return ResponseDto.success(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {
        User user = validateUser(request);
        if (null == user) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 ID 입니다.");
        }
        if (comment.validateUser(user)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
        return ResponseDto.success("comment delete success");
    }


    // 댓글 찾기
    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }

    // 토큰사용시 검증
    @Transactional
    public User validateUser(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getUserFromAuthentication();
    }
}
