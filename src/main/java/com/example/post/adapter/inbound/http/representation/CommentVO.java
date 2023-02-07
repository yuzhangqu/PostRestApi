package com.example.post.adapter.inbound.http.representation;

import com.example.post.domain.contexts.post.Comment;
import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class CommentVO {
    private Long id;
    private String commenter;
    private String content;
    private Instant time;
    private Long postId;

    public CommentVO(String commenter, String content, Long postId) {
        this.commenter = commenter;
        this.content = content;
        this.postId = postId;
    }

    public Comment toDomain() {
        var Comment = new Comment(this.commenter, this.content, this.postId);
        Comment.setId(this.id);
        Comment.setTime(this.time);
        return Comment;
    }

    public static CommentVO fromDomain(Comment comment) {
        var CommentVO = new CommentVO(comment.getCommenter(), comment.getContent(), comment.getPostId());
        CommentVO.setId(comment.getId());
        CommentVO.setTime(comment.getTime());
        return CommentVO;
    }

    public String getCommenter() {
        if (this.commenter == null || this.commenter.isEmpty()) {
            return "匿名";
        }

        return this.commenter;
    }
}