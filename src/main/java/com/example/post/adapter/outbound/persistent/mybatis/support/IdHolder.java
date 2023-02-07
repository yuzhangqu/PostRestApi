package com.example.post.adapter.outbound.persistent.mybatis.support;

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
