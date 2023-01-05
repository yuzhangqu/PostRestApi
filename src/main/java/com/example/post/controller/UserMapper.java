package com.example.post.controller;

import com.example.post.model.*;
import com.example.post.support.mybatis.IdHolder;
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
    int countUsers();
    void insertUser(@Param("user") User user);
    List<Post> selectPostsByAuthor(String account, int from, int size);
    int countPostsByAuthor(String account);
    void insertPost(@Param("holder") IdHolder id, String account, @Param("post") Post post);
    Post selectPost(Long id);
    List<Post> selectPosts(int from, int size);
    int countPosts();
    void clearUser();
    void clearPost();
}
