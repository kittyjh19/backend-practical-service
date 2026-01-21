package com.company.techblog.controller;

import com.company.techblog.dto.LikeRequest;
import com.company.techblog.dto.LikeResponse;
import com.company.techblog.dto.PostDto;
import com.company.techblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<PostDto.Response> createPost(
        @Valid @RequestBody PostDto.Request request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    // 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostDto.Response> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 게시글 목록 조회 (최신글 / 인기글)
    @GetMapping
    public ResponseEntity<List<PostDto.Response>> getPosts(
            @RequestParam(defaultValue = "latest") String sort) {

        if ("popular".equals(sort)) {
            return ResponseEntity.ok(postService.getPopularPosts());
        }

        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostDto.Response> updatePost(
        @PathVariable Long id,
        @Valid @RequestBody PostDto.Request request) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.ok().build();
    }

    // 게시글 좋아요 토글
    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long postId, @Valid @RequestBody LikeRequest request) {
        return ResponseEntity.ok(postService.toggleLike(postId, request.getUserId()));
    }
}