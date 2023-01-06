package com.example.post.model;

import com.example.post.controller.UserMapper;
import com.example.post.support.common.database.EntityList;
import com.example.post.support.mybatis.IdHolder;
import lombok.Data;

import javax.inject.Inject;
import java.util.List;

/**
 * @author yuzhangqu
 */
@Data
public class PostComments extends EntityList<Long, Comment> {
    private Long postId;
    @Inject
    private UserMapper userMapper;

    @Override
    public int size() {
        return userMapper.countCommentsByPostId(postId);
    }

    @Override
    public List<Comment> findEntities(int from, int size) {
        return userMapper.selectCommentsByPostId(postId, from, size);
    }

    public IdHolder<Long> add(Comment comment) {
        IdHolder<Long> idHolder = new IdHolder<>();
        userMapper.insertComment(idHolder, postId, comment);
        return idHolder;
    }
}