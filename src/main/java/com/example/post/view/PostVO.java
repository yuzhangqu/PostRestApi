package com.example.post.view;

import com.example.post.model.Post;
import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class PostVO {
    private String id;
    private String title;
    private String content;
    private Instant time;

    public PostVO(String id, String title, String content, Instant time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public Post toDomain() {
        Post post = new Post(this.id, this.title, this.content, this.time);
        return post;
    }

    public static PostVO fromDomain(Post post) {
        return new PostVO(post.getId(), post.getTitle(), post.getContent(), post.getTime());
    }
}
