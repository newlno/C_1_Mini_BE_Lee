package com.example.C_1_Mini_BE.Controller;


import com.example.C_1_Mini_BE.Dto.Request.PostRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@RestController  // 객체를 JSON 또는 XML 형식으로 HTTP응답에 반환
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/api/post")
    public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                     HttpServletRequest request) {
        return postService.createPost(requestDto, request);
    }

    // 게시글 개별조회
    @GetMapping("/api/post/{id}")
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시글 전체조회
    @GetMapping("/api/post")
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    // 게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    // 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

}
