package com.example.post.model;

import lombok.Data;

/**
 * @author yuzhangqu
 */
@Data
public class User {
    private String account;
    private String name;
    private UserPosts userPosts;

    public User(String account, String name) {
        this.account = account;
        this.name = name;
    }
}
