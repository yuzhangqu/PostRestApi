package com.example.post.model;

import com.example.post.persistent.mybatis.UserMapper;
import com.example.post.persistent.support.mybatis.IdHolder;
import lombok.Data;

import javax.inject.Inject;
import java.util.*;

/**
 * @author yuzhangqu
 */
@Data
public class UserPosts {
    private String userAccount;
    @Inject
    private UserMapper userMapper;

    public List<Post> findEntities(int from, int size) {
        return userMapper.selectPostsByAuthor(userAccount, from, size);
    }

    public void add(Post post) {
        IdHolder idHolder = new IdHolder();
        userMapper.insertPost(idHolder, userAccount, post);
    }
}
