package com.sparta.heartvera.domain.comment.service;

import com.sparta.heartvera.common.exception.CustomException;
import com.sparta.heartvera.common.exception.ErrorCode;
import com.sparta.heartvera.domain.comment.dto.CommentRequestDto;
import com.sparta.heartvera.domain.comment.dto.CommentResponseDto;
import com.sparta.heartvera.domain.comment.entity.Comment;
import com.sparta.heartvera.domain.comment.repository.CommentRepository;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.repository.PostRepository;
import com.sparta.heartvera.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  // 댓글 작성
  public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
    Post post = findPostById(postId);
    Comment comment = commentRepository.save(new Comment(requestDto, post, user));
    return new CommentResponseDto(comment);
  }

  // 댓글 조회
  public List<CommentResponseDto> getComments(Long postId) {
    Post post = findPostById(postId);
    List<Comment> comments = post.getComments();
    List<CommentResponseDto> commentList = new ArrayList<>();
    for (Comment comment : comments) {
      commentList.add(new CommentResponseDto(comment));
    }
    return commentList;
  }

  // 댓글 수정
  @Transactional
  public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto,
      User user) {
    Post post = findPostById(postId);
    Comment comment = findCommentById(commentId);
    if (!comment.getUser().getUserSeq().equals(user.getUserSeq())) {
      throw new CustomException(ErrorCode.COMMENT_NOT_USER);
    }
    comment.updateComment(requestDto);
    return new CommentResponseDto(comment);
  }

  // 댓글 삭제
  public void deleteComment(Long postId, Long commentId, User user) {
    Comment comment = findCommentById(commentId);
    if (!comment.getUser().getUserSeq().equals(user.getUserSeq())) {
      throw new CustomException(ErrorCode.COMMENT_NOT_USER);
    }
    commentRepository.delete(comment);
  }

  public Comment findCommentById(Long commentId) {
    return commentRepository.findById(commentId).orElseThrow(() ->
        new CustomException(ErrorCode.COMMENT_NOT_FOUND));
  }

  private Post findPostById(Long postId) {
    return postRepository.findById(postId).orElseThrow(() ->
        new CustomException(ErrorCode.POST_NOT_FOUND));
  }

}