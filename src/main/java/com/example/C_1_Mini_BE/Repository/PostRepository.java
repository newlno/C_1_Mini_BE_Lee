package com.example.C_1_Mini_BE.Repository;

import com.example.C_1_Mini_BE.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 수정일자 기준 내림차순 정렬
    List<Post> findAllByOrderByModifiedAtDesc();
}
