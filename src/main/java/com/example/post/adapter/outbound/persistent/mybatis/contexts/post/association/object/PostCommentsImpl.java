package com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object;

import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.adapter.outbound.persistent.mybatis.support.database.EntityList;
import com.example.post.adapter.outbound.persistent.mybatis.support.IdHolder;
import com.example.post.domain.contexts.post.Comment;
import com.example.post.domain.contexts.post.association.object.PostComments;
import lombok.Data;

import javax.inject.Inject;
import java.util.List;

/**
 * @author yuzhangqu
 */
@Data
public class PostCommentsImpl extends EntityList<Long, Comment> implements PostComments {
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

    @Override
    public Long add(Comment comment) {
        IdHolder<Long> idHolder = new IdHolder<>();
        userMapper.insertComment(idHolder, postId, comment);
        return idHolder.getId();
    }
}