package com.example.C_1_Mini_BE.Repository;

import com.example.C_1_Mini_BE.Model.Comment;
import com.example.C_1_Mini_BE.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    int countAllByPost(Post post);
}
