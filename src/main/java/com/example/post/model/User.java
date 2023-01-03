package com.example.post.model;

import lombok.Data;

import java.util.List;

/**
 * @author yuzhangqu
 */
@Data
public class User {
    private String account;
    private String name;
    private List<Post> postList;

    public User(String account, String name) {
        this.account = account;
        this.name = name;
    }
}
