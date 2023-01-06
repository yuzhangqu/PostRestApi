package com.example.post.model;

import com.example.post.support.common.HasOne;
import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class Comment {
    private Long id;
    private String commenter;
    private String content;
    private Instant time;
    private HasOne<Post> post;

    public Comment(String commenter, String content) {
        this.commenter = commenter;
        this.content = content;
    }
}
