package com.example.post.domain.concept;

/**
 * @author yuzhangqu
 */
public interface HasMany<ID, T> {
    Many<T> getAll();
}
