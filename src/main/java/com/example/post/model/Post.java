package com.example.post.model;

import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class Post {
    private String id;
    private String title;
    private String author;
    private String content;
    private Instant time;

    public Post(String id, String title, String content, Instant time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public Post(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}
