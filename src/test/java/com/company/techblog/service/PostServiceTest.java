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
}
