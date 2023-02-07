package com.example.post.domain.contexts.post;

import com.example.post.domain.contexts.post.association.object.PostComments;
import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class Post {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Instant time;
    private PostComments postComments;

    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
