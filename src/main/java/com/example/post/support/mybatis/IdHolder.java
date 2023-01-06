package com.example.post.support.mybatis;

import lombok.Data;

/**
 * @author yuzhangqu
 */
@Data
public class IdHolder<T> {
    private T id;

    public String id() {
        return id.toString();
    }
}
