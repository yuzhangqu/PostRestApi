package com.example.post;

import com.example.post.controller.UserMapper;
import com.example.post.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setup() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"account\":\"zhangsan\", \"name\":\"张三\"}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"account\":\"lisi\", \"name\":\"李四\"}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"account\":\"wangmazi\", \"name\":\"王麻子\"}"));

        mockMvc.perform(post("/users/zhangsan/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"title\":\"张三的文章1\", \"content\":\"张三的文章1的内容\"}"));
        mockMvc.perform(post("/users/zhangsan/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"title\":\"张三的文章2\", \"content\":\"张三的文章2的内容\"}"));
        mockMvc.perform(post("/users/zhangsan/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"title\":\"张三的文章3\", \"content\":\"张三的文章3的内容\"}"));
        mockMvc.perform(post("/users/lisi/posts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"title\":\"李四的文章1\", \"content\":\"李四的文章1的内容\"}"));

        mockMvc.perform(post("/posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"commenter\":\"网友1\", \"content\":\"张三文章1的评论1\"}"));
        mockMvc.perform(post("/posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"commenter\":\"网友2\", \"content\":\"张三文章1的评论2\"}"));
        mockMvc.perform(post("/posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"commenter\":\"网友3\", \"content\":\"张三文章1的评论3\"}"));
    }

    @AfterEach
    void teardown() {
        userMapper.clearUser();
        userMapper.clearPost();
        userMapper.clearComment();
    }

    @Test
    void testCreateUser() throws Exception {
        var originCount = userMapper.countUsers();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"account\":\"newbie\", \"name\":\"新人\"}"))
                .andExpect(status().isCreated());
        assertThat(userMapper.countUsers()).isEqualTo(originCount + 1);
    }

    @Test
    void testCreateUserConflict() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"account\":\"zhangsan\", \"name\":\"张三\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(get("/users")
                        .queryParam("page", "2")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(1)));

        mockMvc.perform(get("/users")
                        .queryParam("page", "3")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/users/zhangsan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value("zhangsan"))
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void testGetUserNotExist() throws Exception {
        mockMvc.perform(get("/users/wrongAccount"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePost() throws Exception {
        var user = userMapper.selectUser("lisi");
        var originCount = user.getUserPosts().size();
        mockMvc.perform(post("/users/lisi/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\":\"李四的文章2\", \"content\":\"李四的文章2的内容\"}"))
                .andExpect(status().isCreated());
        assertThat(user.getUserPosts().size()).isEqualTo(originCount + 1);
    }

    @Test
    void testCreatePostAuthorNotExist() throws Exception {
        mockMvc.perform(post("/users/wrongAccount/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\":\"新文章\", \"content\":\"新文章的内容\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserPosts() throws Exception {
        mockMvc.perform(get("/users/zhangsan/posts")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(get("/users/zhangsan/posts")
                        .queryParam("page", "2")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(1)));

        mockMvc.perform(get("/users/zhangsan/posts")
                        .queryParam("page", "3")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void testGetUserPostsAuthorNotExist() throws Exception {
        mockMvc.perform(get("/users/wrongAccount/posts")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPosts() throws Exception {
        mockMvc.perform(get("/posts")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(get("/posts")
                        .queryParam("page", "2")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(get("/posts")
                        .queryParam("page", "3")
                        .queryParam("size", "2"))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void testGetPost() throws Exception {
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("张三的文章1"));
    }

    @Test
    void testGetPostNotExist() throws Exception {
        mockMvc.perform(get("/posts/404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostComments() throws Exception {
        mockMvc.perform(get("/posts/1/comments")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(get("/posts/1/comments")
                        .queryParam("page", "2")
                        .queryParam("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));

        mockMvc.perform(get("/posts/1/comments")
                        .queryParam("page", "3")
                        .queryParam("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void testGetPostCommentsPostNotExist() throws Exception {
        mockMvc.perform(get("/posts/404/comments")
                        .queryParam("page", "1")
                        .queryParam("size", "2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateComment() throws Exception {
        var post = userMapper.selectPost(1L);
        var originCount = post.getPostComments().size();
        mockMvc.perform(post("/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"content\":\"匿名评论1\"}"))
                .andExpect(status().isCreated());
        assertThat(post.getPostComments().size()).isEqualTo(originCount + 1);
    }

    @Test
    void testCreateCommentPostNotExist() throws Exception {
        mockMvc.perform(post("/posts/404/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"content\":\"新评论\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetComment() throws Exception {
        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("张三文章1的评论1"));
    }
}
