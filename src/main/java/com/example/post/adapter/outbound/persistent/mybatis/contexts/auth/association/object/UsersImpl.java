package com.example.post.adapter.outbound.persistent.mybatis.contexts.auth.association.object;

import com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object.PostContextImpl;
import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.adapter.outbound.persistent.mybatis.support.database.EntityList;
import com.example.post.domain.contexts.auth.User;
import com.example.post.domain.contexts.auth.association.object.Users;
import com.example.post.domain.contexts.post.PostContext;

import javax.inject.Inject;
import java.util.List;

public class UsersImpl extends EntityList<String, User> implements Users {
    @Inject
    private UserMapper userMapper;

    @Inject
    private PostContextImpl postContext;

    @Override
    protected List<User> findEntities(int from, int to) {
        return userMapper.selectUsers(from, to);
    }

    @Override
    public int size() {
        return userMapper.countUsers();
    }

    @Override
    public User findUserByAccount(String account) {
        return userMapper.selectUser(account);
    }

    @Override
    public PostContext inPostContext() {
        return postContext;
    }
}
