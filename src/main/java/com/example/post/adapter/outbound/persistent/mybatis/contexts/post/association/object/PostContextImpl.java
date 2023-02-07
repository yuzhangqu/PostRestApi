package com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object;

import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.domain.contexts.auth.User;
import com.example.post.domain.contexts.post.PostContext;
import com.example.post.domain.contexts.post.Poster;

import javax.inject.Inject;

public class PostContextImpl implements PostContext {
    @Inject
    private UserMapper userMapper;

    @Override
    public Poster asPoster(User user) {
        return userMapper.selectPoster(user.getAccount());
    }
}
