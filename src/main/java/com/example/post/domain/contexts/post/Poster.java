package com.example.post.domain.contexts.post;

import com.example.post.domain.contexts.post.association.object.UserPosts;
import lombok.Data;

@Data
public class Poster {
    private String account;
    private UserPosts userPosts;

    public Poster(String account) {
        this.account = account;
    }
}
