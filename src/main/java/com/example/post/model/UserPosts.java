package com.example.post.model;

import com.example.post.UserMapper;
import com.example.post.support.common.database.EntityList;
import com.example.post.support.mybatis.IdHolder;
import lombok.Data;

import javax.inject.Inject;
import java.util.*;

/**
 * @author yuzhangqu
 */
@Data
public class UserPosts extends EntityList<String, Post> {
    private String userAccount;
    @Inject
    private UserMapper userMapper;

    @Override
    public int size() {
        return userMapper.countPostByAuthor(userAccount);
    }

    @Override
    protected Post findEntity(String id) {
        return userMapper.selectPostById(id);
    }

    public List<Post> findEntities(int from, int size) {
        return userMapper.selectPostsByAuthor(userAccount, from, size);
    }

    public void add(Post post) {
        IdHolder idHolder = new IdHolder();
        userMapper.insertPost(idHolder, userAccount, post);
    }
}
