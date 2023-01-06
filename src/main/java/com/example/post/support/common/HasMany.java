package com.example.post.support.common;

/**
 * @author yuzhangqu
 */
public interface HasMany<ID, T> {
    Many<T> getAll();
}
