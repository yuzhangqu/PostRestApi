package com.example.post.support.mybatis;

import lombok.Data;

/**
 * @author yuzhangqu
 */
@Data
public class IdHolder {
    private Long id;

    public String id() {
        return id.toString();
    }
}
