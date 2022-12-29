package com.example.post.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Comment {
    private String id;
    private String author;
    private String postId;
    private String content;
    private Instant time;

    public Comment(String author, String postId, String content) {
        this.author = author;
        this.postId = postId;
        this.content = content;
    }
}
