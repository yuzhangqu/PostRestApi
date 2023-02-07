package com.example.post.domain.concept;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author yuzhangqu
 */
public interface Many<T> extends Iterable<T> {
    int size();

    Many<T> subCollection(int from, int to);

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
