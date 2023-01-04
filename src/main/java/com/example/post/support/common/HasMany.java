package com.example.post.support.common;

import java.util.Optional;

/**
 * @author yuzhangqu
 */
public interface HasMany<ID, T> {
    Many<T> findAll();

    Optional<T> findById(ID identifier);
}
