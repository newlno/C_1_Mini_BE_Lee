package com.example.C_1_Mini_BE.Controller;


import com.example.C_1_Mini_BE.Dto.Request.CommentRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@RestController  // 객체를 JSON 또는 XML 형식으로 HTTP응답에 반환
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/api/comment")
    public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    // 댓글 수정
    @PutMapping("/api/comment/{commentid}")
    public ResponseDto<?> updateComment(@PathVariable Long commentid, @RequestBody CommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return commentService.updateComment(commentid, requestDto, request);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comment/{commentid}")
    public ResponseDto<?> deleteComment(@PathVariable Long commentid,
                                        HttpServletRequest request) {
        return commentService.deleteComment(commentid, request);
    }
}
