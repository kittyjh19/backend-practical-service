package com.company.techblog.service;

import com.company.techblog.domain.User;
import com.company.techblog.dto.PostDto;
import com.company.techblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 퇴사자는_게시글을_작성할_수_없다() {

        //given
        User user = userRepository.save(
                User.builder()
                        .email("resigned@test.com")
                        .name("퇴사자")
                        .build()
        );
        user.resign();

        PostDto.Request request =
                new PostDto.Request(user.getId(), "제목", "내용");

        // when & then
        assertThatThrownBy(() -> postService.createPost(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("퇴사한 사용자는 게시글을 작성할 수 없습니다.");
    }

    @Test
    void 재직자는_게시글을_작성할_수_있다() {

        //given
        User user = userRepository.save(
                User.builder()
                        .email("active@test.com")
                        .name("재직자")
                        .build()
        );

        PostDto.Request request =
                new PostDto.Request(user.getId(), "정상 제목", "정상 내용");

        // when
        PostDto.Response response = postService.createPost(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getAuthorId()).isEqualTo(user.getId());
    }

    @Test
    void 좋아요는_토글_형식으로_동작한다() {

        // given
        User author = userRepository.save(
                User.builder()
                        .email("author@test.com")
                        .name("작성자")
                        .build()
        );

        User liker = userRepository.save(
                User.builder()
                        .email("liker@test.com")
                        .name("좋아요유저")
                        .build()
        );

        PostDto.Request postRequest =
                new PostDto.Request(author.getId(), "제목", "내용");

        Long postId = postService.createPost(postRequest).getId();

        // when - 좋아요 추가
        var likeResponse1 = postService.toggleLike(postId, liker.getId());

        // then
        assertThat(likeResponse1.isLiked()).isTrue();
        assertThat(likeResponse1.getLikeCount()).isEqualTo(1);

        // when - 다시 좋아요 (취소)
        var likeResponse2 = postService.toggleLike(postId, liker.getId());

        // then
        assertThat(likeResponse2.isLiked()).isFalse();
        assertThat(likeResponse2.getLikeCount()).isEqualTo(0);
    }

    @Test
    void 본인_게시글에는_좋아요를_누를_수_없다() {

        // given
        User author = userRepository.save(
                User.builder()
                        .email("self@test.com")
                        .name("본인")
                        .build()
        );

        PostDto.Request postRequest =
                new PostDto.Request(author.getId(), "제목", "내용");

        Long postId = postService.createPost(postRequest).getId();

        // when & then
        assertThatThrownBy(() -> postService.toggleLike(postId, author.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("본인 게시글에는 좋아요를 누를 수 없습니다.");
    }

    @Test
    void 인기게시글은_좋아요_개수_내림차순으로_정렬된다() {

        // given
        User author = userRepository.save(
                User.builder()
                        .email("author@test.com")
                        .name("작성자")
                        .build()
        );

        User liker1 = userRepository.save(
                User.builder()
                        .email("liker1@test.com")
                        .name("좋아요1")
                        .build()
        );

        User liker2 = userRepository.save(
                User.builder()
                        .email("liker2@test.com")
                        .name("좋아요2")
                        .build()
        );

        Long postId1 = postService.createPost(
                new PostDto.Request(author.getId(), "게시글1", "내용1")
        ).getId();

        Long postId2 = postService.createPost(
                new PostDto.Request(author.getId(), "게시글2", "내용2")
        ).getId();

        Long postId3 = postService.createPost(
                new PostDto.Request(author.getId(), "게시글3", "내용3")
        ).getId();

        postService.toggleLike(postId1, liker1.getId());
        postService.toggleLike(postId1, liker2.getId());
        postService.toggleLike(postId2, liker1.getId());

        // when
        var popularPosts = postService.getPopularPosts();

        // then - 내가 만든 게시글만 필터링
        var myPosts = popularPosts.stream()
                .filter(p -> p.getId().equals(postId1)
                        || p.getId().equals(postId2)
                        || p.getId().equals(postId3))
                .toList();

        assertThat(myPosts).hasSize(3);

        assertThat(myPosts.get(0).getId()).isEqualTo(postId1); // 좋아요 2
        assertThat(myPosts.get(1).getId()).isEqualTo(postId2); // 좋아요 1
        assertThat(myPosts.get(2).getId()).isEqualTo(postId3); // 좋아요 0
    }



}
