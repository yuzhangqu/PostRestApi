package com.example.post.support.common;

import lombok.Data;

/**
 * @author yuzhangqu
 */
@Data
public class Pagination<T> {
    private int pageSize;
    private Many<T> many;
    private final int total;

    public Pagination(int pageSize, Many<T> many) {
        this.pageSize = pageSize;
        this.many = many;
        this.total = many.size();
    }

    public Many<T> page(int pageNum) {
        Many<T> current = many.subCollection(pageNum * pageSize, Math.min(total, (pageNum + 1) * pageSize));
        return current;
    }
}
