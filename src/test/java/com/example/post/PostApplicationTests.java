package com.example.post;

import com.example.post.controller.UserService;
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
    private UserService userService;

    @BeforeEach
    void setup() {
        userService.addUser(new User("zhangsan", "张三"));
        userService.addUser(new User("lisi", "李四"));
        userService.addUser(new User("wangmazi", "王麻子"));
        userService.addPost(new Post("zhangsan", "张三的文章1", "张三的文章1的内容"));
        userService.addPost(new Post("zhangsan", "张三的文章2", "张三的文章2的内容"));
        userService.addComment(new Comment("lisi", "1", "李四对张三文章1的评论1"));
        userService.addComment(new Comment("lisi", "1", "李四对张三文章1的评论2"));
    }

    @AfterEach
    void teardown() {
        userService.clear();
    }

    @Test
    void testCreateUser() throws Exception {
        var originCount = userService.countUsers();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"account\":\"neo\", \"name\":\"新人\"}"))
                .andExpect(status().isCreated());
        assertThat(userService.countUsers()).isEqualTo(originCount + 1);
    }

    @Test
    void testCreateUserConflict() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"account\":\"zhangsan\", \"name\":\"新人\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    void testGetUsersPage() throws Exception {
        mockMvc.perform(get("/users")
                        .queryParam("page", "2")
                        .queryParam("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void testGetUser() throws Exception {
        mockMvc.perform(get("/users/zhangsan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void testGetUserNotExist() throws Exception {
        mockMvc.perform(get("/users/wrongAccount"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserPosts() throws Exception {
        mockMvc.perform(get("/users/zhangsan/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testGetUserPostsNotExist() throws Exception {
        mockMvc.perform(get("/users/wrongAccount/posts"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePost() throws Exception {
        var originCount = userService.countPosts();
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"author\":\"lisi\", \"title\":\"李四的文章1\", \"content\":\"李四的文章1的内容\"}"))
                .andExpect(status().isCreated());
        assertThat(userService.countPosts()).isEqualTo(originCount + 1);
    }

    @Test
    void testCreatePostAuthorNotExist() throws Exception {
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"author\":\"wrongAccount\", \"title\":\"新文章\", \"content\":\"新文章的内容\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testGetPostsPage() throws Exception {
        mockMvc.perform(get("/posts")
                        .queryParam("page", "2")
                        .queryParam("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void testGetPost() throws Exception {
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("张三的文章1"));
    }

    @Test
    void testGetPostComments() throws Exception {
        mockMvc.perform(get("/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void testCreateComment() throws Exception {
        var originCount = userService.countPostComments("1");
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"author\":\"zhangsan\", \"postId\":\"1\", \"content\":\"张三对张三文章1的评论1\"}"))
                .andExpect(status().isCreated());
        assertThat(userService.countPostComments("1")).isEqualTo(originCount + 1);
    }

    @Test
    void testCreateCommentAuthorNotExist() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"author\":\"wrongAccount\", \"postId\":\"1\", \"content\":\"新人对张三文章1的评论1\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateCommentPostNotExist() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"author\":\"zhangsan\", \"postId\":\"404\", \"content\":\"张三的评论\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetComment() throws Exception {
        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("李四对张三文章1的评论1"));
    }
}
