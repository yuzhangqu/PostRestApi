package com.example.post.model;

import com.example.post.controller.UserMapper;
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
        return userMapper.countPostsByAuthor(userAccount);
    }

    @Override
    public List<Post> findEntities(int from, int size) {
        return userMapper.selectPostsByAuthor(userAccount, from, size);
    }

    public IdHolder add(Post post) {
        IdHolder idHolder = new IdHolder();
        userMapper.insertPost(idHolder, userAccount, post);
        return idHolder;
    }
}
