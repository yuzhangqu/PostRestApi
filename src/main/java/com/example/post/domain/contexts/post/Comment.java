package com.example.post.domain.contexts.post;

import com.example.post.domain.concept.HasOne;
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
    private Long postId;
    private Instant time;

    public Comment(String commenter, String content, Long postId) {
        this.commenter = commenter;
        this.content = content;
        this.postId = postId;
    }
}
