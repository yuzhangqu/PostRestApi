package com.example.post.domain.contexts.auth;

import lombok.Data;

/**
 * @author yuzhangqu
 */
@Data
public class User {
    private String account;
    private String name;

    public User(String account, String name) {
        this.account = account;
        this.name = name;
    }
}
