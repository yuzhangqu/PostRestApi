package com.example.post.adapter.inbound.http.representation;

import com.example.post.domain.contexts.post.Post;
import lombok.Data;

import java.time.Instant;

/**
 * @author yuzhangqu
 */
@Data
public class PostVO {
    private Long id;
    private String title;
    private String content;
    private Instant time;
    private String author;

    public PostVO(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Post toDomain() {
        var post = new Post(this.title, this.content, this.author);
        post.setId(this.id);
        post.setTime(this.time);
        return post;
    }

    public static PostVO fromDomain(Post post) {
        var postVO = new PostVO(post.getTitle(), post.getContent(), post.getAuthor());
        postVO.setId(post.getId());
        postVO.setTime(post.getTime());
        return postVO;
    }
}
