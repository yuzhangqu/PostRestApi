package com.example.post.adapter.outbound.persistent.mybatis.mapper;

import com.example.post.domain.contexts.auth.User;
import com.example.post.domain.contexts.post.Comment;
import com.example.post.domain.contexts.post.Post;
import com.example.post.adapter.outbound.persistent.mybatis.support.IdHolder;
import com.example.post.domain.contexts.post.Poster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuzhangqu
 */
@Mapper
public interface UserMapper {
    User selectUser(String account);
    List<User> selectUsers(int from, int size);
    Poster selectPoster(String account);
    int countUsers();
    void insertUser(@Param("user") User user);
    List<Post> selectPostsByAuthor(String account, int from, int size);
    int countPostsByAuthor(String account);
    void insertPost(@Param("holder") IdHolder<Long> id, String account, @Param("post") Post post);
    Post selectPost(Long id);
    List<Post> selectPosts(int from, int size);
    int countPosts();
    List<Comment> selectCommentsByPostId(Long postId, int from, int size);
    int countCommentsByPostId(Long postId);
    void insertComment(@Param("holder") IdHolder<Long> id, Long postId, @Param("comment") Comment comment);
    Comment selectComment(Long id);

    void clearUser();
    void clearPost();
    void clearComment();
}
