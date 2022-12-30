package com.example.post.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yuzhangqu
 */
@Getter
@Setter
public class User {
    private String account;
    private String name;

    public User(String account, String name) {
        this.account = account;
        this.name = name;
    }
}
