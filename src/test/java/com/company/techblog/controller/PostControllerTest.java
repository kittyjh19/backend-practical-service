package com.company.techblog.controller;

import com.company.techblog.dto.LikeResponse;
import com.company.techblog.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Test
    void 게시글_좋아요_토글_API_정상_동작() throws Exception {

        // given
        LikeResponse response = LikeResponse.builder()
                .postId(1L)
                .liked(true)
                .likeCount(5L)
                .build();

        given(postService.toggleLike(1L, 10L))
                .willReturn(response);

        // when & then
        mockMvc.perform(
                        post("/api/posts/{postId}/likes", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                          "userId": 10
                        }
                    """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.liked").value(true))
                .andExpect(jsonPath("$.likeCount").value(5));
    }

}
