package com.example.post.model;

import lombok.Data;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author yuzhangqu
 */
@Data
public class UserPosts {
    private String userAccount;
    private List<Post> posts;

    public List<Post> findEntities(int from, int to) {
        return this.posts.subList(from, to);
    }

    public Stream<Post> stream() {
        return this.posts.stream();
    }
}
