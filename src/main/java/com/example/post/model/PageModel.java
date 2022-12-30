package com.example.post.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yuzhangqu
 */
public class PageModel<T> {
    @JsonProperty
    private T data;

    @JsonProperty
    private Page page;

    public PageModel(T data, int pageNumber, int size, int total) {
        this.data = data;
        this.page = new Page(pageNumber, size, total);
    }

    private class Page {
        @JsonProperty
        private final int pageNumber;

        @JsonProperty
        private final int size;

        @JsonProperty
        private final int total;

        public Page(int pageNumber, int size, int total) {
            this.pageNumber = pageNumber;
            this.size = size;
            this.total = total;
        }
    }
}
