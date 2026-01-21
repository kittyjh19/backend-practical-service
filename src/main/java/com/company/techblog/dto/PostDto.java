package com.company.techblog.dto;

import com.company.techblog.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PostDto {

    @Getter
    @RequiredArgsConstructor
    public static class Request {

        @NotNull
        private final Long userId;

        @NotBlank(message = "제목은 필수입니다")
        @Size(min = 1, max = 100, message = "제목은 1-100자 사이여야 합니다")
        private final String title;

        @NotBlank(message = "내용은 필수입니다")
        private final String content;

    }

    @Getter
    @Builder
    public static class Response {
        private final Long id;
        private final Long authorId;
        private final String author;
        private final String title;
        private final String content;
        private final Long likeCount;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;

        public static Response from(Post post) {
            return Response.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .author(post.getAuthor().getName())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        }
    }
}