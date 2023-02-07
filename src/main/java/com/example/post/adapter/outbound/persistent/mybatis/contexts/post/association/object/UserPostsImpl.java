package com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object;

import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.adapter.outbound.persistent.mybatis.support.database.EntityList;
import com.example.post.adapter.outbound.persistent.mybatis.support.IdHolder;
import com.example.post.domain.contexts.post.Post;
import com.example.post.domain.contexts.post.association.object.UserPosts;
import lombok.Data;

import javax.inject.Inject;
import java.util.*;

/**
 * @author yuzhangqu
 */
@Data
public class UserPostsImpl extends EntityList<String, Post> implements UserPosts {
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

    @Override
    public Long add(Post post) {
        IdHolder<Long> idHolder = new IdHolder<>();
        userMapper.insertPost(idHolder, userAccount, post);
        return idHolder.getId();
    }
}
