package com.example.post.view;

import com.example.post.model.Comment;
import com.example.post.support.common.memory.Reference;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentVO {
    private Long id;
    private String commenter;
    private String content;
    private Instant time;
    private PostVO post;

    public CommentVO(String commenter, String content) {
        this.commenter = commenter;
        this.content = content;
    }

    public Comment toDomain() {
        var Comment = new Comment(this.commenter, this.content);
        Comment.setId(this.id);
        Comment.setTime(this.time);
        if (this.post != null) {
            Comment.setPost(new Reference<>(this.post.toDomain()));
        }
        return Comment;
    }

    public static CommentVO fromDomain(Comment comment) {
        var CommentVO = new CommentVO(comment.getCommenter(), comment.getContent());
        CommentVO.setId(comment.getId());
        CommentVO.setTime(comment.getTime());
        if (comment.getPost().get() != null) {
            CommentVO.setPost(PostVO.fromDomain(comment.getPost().get()));
        }
        return CommentVO;
    }
}