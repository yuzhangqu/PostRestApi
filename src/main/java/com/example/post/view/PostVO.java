package com.example.post.view;

import com.example.post.model.Post;
import com.example.post.support.common.memory.Reference;
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
    private UserVO author;

    public PostVO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toDomain() {
        var post = new Post(this.title, this.content);
        post.setId(this.id);
        post.setTime(this.time);
        if (this.author != null) {
            post.setAuthor(new Reference<>(this.author.toDomain()));
        }
        return post;
    }

    public static PostVO fromDomain(Post post) {
        var postVO = new PostVO(post.getTitle(), post.getContent());
        postVO.setId(post.getId());
        postVO.setTime(post.getTime());
        if (post.getAuthor().get() != null) {
            postVO.setAuthor(UserVO.fromDomain(post.getAuthor().get()));
        }
        return postVO;
    }
}
