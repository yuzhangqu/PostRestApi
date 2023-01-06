package com.example.post.support.common.memory;

import com.example.post.support.common.HasOne;

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