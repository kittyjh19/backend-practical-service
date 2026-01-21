package com.company.techblog.repository;

import com.company.techblog.domain.Post;
import com.company.techblog.domain.PostLike;
import com.company.techblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<PostLike, Long> {

    // 좋아요 토글 여부 판단용
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

}
