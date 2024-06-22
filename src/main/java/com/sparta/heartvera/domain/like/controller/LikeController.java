package com.sparta.heartvera.domain.like.controller;

import com.sparta.heartvera.domain.like.dto.LikeCountResponseDto;
import com.sparta.heartvera.domain.like.entity.LikeEnum;
import com.sparta.heartvera.domain.like.service.LikeService;
import com.sparta.heartvera.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class LikeController {

    private final LikeService likeService;

    // 게시물별 좋아요 수 조회
    @GetMapping("/{postId}/like/count")
    public LikeCountResponseDto getPostLikes(@PathVariable("postId") Long postId) {
        return new LikeCountResponseDto(likeService.getLikesCount(postId, LikeEnum.POST));
    }

    // 댓글별 좋아요 수 조회
    @GetMapping("/{postId}/comments/{commentsId}/like/count")
    public LikeCountResponseDto getCommentLikes(@PathVariable("commentsId") Long commentsId) {
        return new LikeCountResponseDto(likeService.getLikesCount(commentsId, LikeEnum.COMMENT));
    }

    // 게시물별 좋아요 토글
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> togglePostLike(@PathVariable(name = "postId") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.togglePostLike(userDetails.getUser().getUserSeq(), postId);
    }

    // 댓글별 좋아요 토글
    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<String> toggleCommentLike(@PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.toggleCommentLike(userDetails.getUser().getUserSeq(), commentId);
    }

    // 게시물별 좋아요 삭제
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> deletePostLike(@PathVariable(name = "postId") long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return likeService.deletePostLike(userDetails.getUser().getUserSeq(), postId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글별 좋아요 삭제
    @DeleteMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<String> deleteCommentLike(@PathVariable(name = "commentId") long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            return likeService.deleteCommentLike(userDetails.getUser().getUserSeq(), commentId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}