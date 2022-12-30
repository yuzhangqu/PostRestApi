package com.example.post.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Getter
@Setter
public class Post {
    private String id;
    private String author;
    private String title;
    private String content;
    private Instant time;

    public Post(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}
