package com.sparta.spring3.service;

import com.sparta.spring3.controller.response.ResponseDto;
import com.sparta.spring3.controller.response.CommentResponseDto;
import com.sparta.spring3.domain.Comment;
import com.sparta.spring3.domain.Member;
import com.sparta.spring3.domain.Post;
import com.sparta.spring3.controller.request.CommentRequestDto;
import com.sparta.spring3.domain.User;
import com.sparta.spring3.jwt.TokenProvider;
import com.sparta.spring3.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final TokenProvider tokenProvider;
  private final PostService postService;

  @Transactional
  public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    User user = validateUser(request);
    if (null == user) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post postKey = postService.isPresentPost(requestDto.getPostKey());
    if (null == postKey) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = Comment.builder()
        .user(user)
        .post(postKey)
        .comment(requestDto.getComment())
        .build();
    commentRepository.save(comment);
    return ResponseDto.success(
        CommentResponseDto.builder()
            .commentKey(comment.getCommentKey())
            .userId(comment.getMember().getUserKey())
            .content(comment.getComment())
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllCommentsByPost(Long postId) {
    Post post = postService.isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      commentResponseDtoList.add(
          CommentResponseDto.builder()
              .commentKey(comment.getCommentKey())
              .userId(comment.getMember().getNickname())
              .commentKey(comment.getCommentKey())
              .createdAt(comment.getCreatedAt())
              .modifiedAt(comment.getModifiedAt())
              .build()
      );
    }
    return ResponseDto.success(commentResponseDtoList);
  }

  @Transactional
  public ResponseDto<?> updateComment(Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = postService.isPresentPost(requestDto.getPostKey());
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = isPresentComment(commentId);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }

    if (comment.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    comment.update(requestDto);
    return ResponseDto.success(
        CommentResponseDto.builder()
            .commentKey(comment.getCommentKey())
            .userId(comment.getUser().getUserName())
            .comment(comment.getComment())
            .createdAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .build()
    );
  }

  @Transactional
  public ResponseDto<?> deleteComment(Long commentId, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    User user = validateUser(request);
    if (null == user) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(commentId);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }

    if (comment.validateMember(user)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    commentRepository.delete(comment);
    return ResponseDto.success("success");
  }

  @Transactional(readOnly = true)
  public Comment isPresentComment(Long id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    return optionalComment.orElse(null);
  }

  @Transactional
  public User validateUser(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getUserFromAuthentication();
  }
}
