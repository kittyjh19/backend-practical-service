package com.company.techblog.repository;

import com.company.techblog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        select p
        from Post p
        join fetch p.author
    """)
    List<Post> findAllWithAuthor();

    List<Post> findTop10ByOrderByLikeCountDescCreatedAtDesc();
}