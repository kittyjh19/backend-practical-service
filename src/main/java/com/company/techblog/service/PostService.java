package com.company.techblog.service;

import com.company.techblog.domain.Post;
import com.company.techblog.domain.PostLike;
import com.company.techblog.domain.User;
import com.company.techblog.dto.LikeResponse;
import com.company.techblog.dto.PostDto;
import com.company.techblog.dto.PostDto.Response;
import com.company.techblog.repository.LikeRepository;
import com.company.techblog.repository.PostRepository;
import com.company.techblog.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public PostDto.Response createPost(PostDto.Request request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isResigned()) {
            throw new IllegalStateException("퇴사한 사용자는 게시글을 작성할 수 없습니다.");
        }

        Post post = Post.builder()
                .author(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return PostDto.Response.from(postRepository.save(post));
    }

    public PostDto.Response getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostDto.Response.from(post);
    }

    public List<Response> getAllPosts() {
        return postRepository.findAllWithAuthor().stream()
                .map(PostDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto.Response updatePost(Long postId, PostDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.isAuthor(request.getUserId())) {
            throw new IllegalStateException("Only author can update post");
        }

        if (post.isAuthorResigned()) {
            throw new IllegalStateException("퇴사한 사용자는 게시글을 수정할 수 없습니다.");
        }

        post.update(request.getTitle(), request.getContent());
        return PostDto.Response.from(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.isAuthor(userId)) {
            throw new IllegalStateException("Only author can delete post");
        }

        if (post.isAuthorResigned()) {
            throw new IllegalStateException("퇴사한 사용자는 게시글을 삭제할 수 없습니다.");
        }

        postRepository.delete(post);
    }

    @Transactional
    public LikeResponse toggleLike(Long postId, Long userId) {

        // 1. 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 2. 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 3. 본인 게시글 좋아요 방지
        if (post.isAuthor(userId)) {
            throw new IllegalStateException("본인 게시글에는 좋아요를 누를 수 없습니다.");
        }

        // 4. 해당 게시글에 대한 사용자의 기존 좋아요 존재 여부 조회
        Optional<PostLike> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);

        boolean liked;
        if (existingLike.isPresent()) {
            // 5-1. 이미 좋아요한 상태 → 좋아요 취소
            likeRepository.delete(existingLike.get());
            post.decreaseLikeCount();
            liked = false;
        } else {
            // 5-2. 좋아요하지 않은 상태 → 좋아요 추가
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(postLike);
            post.increaseLikeCount();
            liked = true;
        }

        // 6. 좋아요 처리 결과 반환
        return LikeResponse.builder()
                .postId(postId)
                .likeCount(post.getLikeCount())
                .liked(liked)
                .build();
    }

    // 인기글 상위 10개 조회
    public List<PostDto.Response> getPopularPosts() {
        return postRepository.findTop10ByOrderByLikeCountDescCreatedAtDesc()
                .stream()
                .map(PostDto.Response::from)
                .toList();
    }

}

