package com.example.post.model;

import com.example.post.support.common.HasOne;
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
    private Instant time;
    private HasOne<User> author;
    private PostComments postComments;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
