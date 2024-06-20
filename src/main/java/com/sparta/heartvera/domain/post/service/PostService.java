package com.sparta.heartvera.domain.post.service;

import com.sparta.heartvera.domain.post.dto.PostRequestDto;
import com.sparta.heartvera.domain.post.dto.PostResponseDto;
import com.sparta.heartvera.domain.post.entity.Post;
import com.sparta.heartvera.domain.post.repository.PostRepository;
import com.sparta.heartvera.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostResponseDto savePost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));

        return new PostResponseDto(post);
    }

    public PostResponseDto getPost(Long postId) {
        Post post = findById(postId);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto editPost(Long postId, PostRequestDto requestDto, User user) {
        Post post = findById(postId);
        checkUserSame(post, user);
        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public String deletePost(Long postId, User user) {
        Post post = findById(postId);
        checkUserSame(post, user);
        delete(post);

        return postId + "번 게시물 삭제 완료";
    }

    public Object getAllPost(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Post> postList = postRepository.findAll(pageable);

        if (postList.getTotalElements() == 0) {
            return "아직 등록된 게시물이 없습니다.";
        }

        return postList.map(PostResponseDto::new);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
    }

    private void checkUserSame(Post post, User user) {
        if (!(post.getUser().getUserSeq().equals(user.getUserSeq()))) {
            throw new IllegalArgumentException("작성자만 접근할 수 있습니다.");
        }
    }

    public Object getAllPostForAdmin(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Post> postList = postRepository.findAll(pageable);

        if (postList.getTotalElements() == 0) {
            return "아직 등록된 게시물이 없습니다.";
        }

        return postList;
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }
}