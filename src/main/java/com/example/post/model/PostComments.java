package com.example.post.model;

import com.example.post.controller.UserMapper;
import com.example.post.support.common.database.EntityList;
import com.example.post.support.mybatis.IdHolder;
import lombok.Data;

import javax.inject.Inject;
import java.util.List;

@Data
public class PostComments extends EntityList<Long, Post> {
    private Long postId;
    @Inject
    private UserMapper userMapper;

    @Override
    public int size() {
        return userMapper.countCommentsByPostId(postId);
    }

    @Override
    public List<Post> findEntities(int from, int size) {
        return userMapper.selectCommentsByPostId(postId, from, size);
    }

    public IdHolder add(Comment comment) {
        IdHolder idHolder = new IdHolder();
        userMapper.insertComment(idHolder, postId, comment);
        return idHolder;
    }
}