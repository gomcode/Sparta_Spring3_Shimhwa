package com.sparta.spring3.repository;

import com.sparta.spring3.domain.Comment;
import com.sparta.spring3.domain.Post;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
}
