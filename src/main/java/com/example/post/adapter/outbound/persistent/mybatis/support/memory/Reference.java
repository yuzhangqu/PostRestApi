package com.example.post.adapter.outbound.persistent.mybatis.support.memory;

import com.example.post.domain.concept.HasOne;

/**
 * @author yuzhangqu
 */
public class Reference<T> implements HasOne<T> {
    private T entity;

    public Reference(T entity) {
        this.entity = entity;
    }

    @Override
    public T get() {
        return entity;
    }
}