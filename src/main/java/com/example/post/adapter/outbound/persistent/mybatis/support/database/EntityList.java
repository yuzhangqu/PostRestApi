package com.example.post.adapter.outbound.persistent.mybatis.support.database;

import com.example.post.domain.concept.HasMany;
import com.example.post.domain.concept.Many;

import java.util.Iterator;
import java.util.List;

/**
 * @author yuzhangqu
 */
public abstract class EntityList<ID, T> implements Many<T>, HasMany<ID, T> {
    protected abstract List<T> findEntities(int from, int to);

    @Override
    public Many<T> subCollection(int from, int to) {
        return new com.example.post.adapter.outbound.persistent.mybatis.support.memory.EntityList<>(findEntities(from, to));
    }

    @Override
    public final Many<T> getAll() {
        return this;
    }

    @Override
    public final Iterator<T> iterator() {
        return new BatchIterator();
    }

    protected int batchSize() {
        return 50;
    }

    private class BatchIterator implements Iterator<T> {
        private Iterator<T> iterator;
        private int size;
        private int current = 0;

        public BatchIterator() {
            this.size = size();
            this.iterator = nextBatch();
        }

        private Iterator<T> nextBatch() {
            return subCollection(current, Math.min(current + batchSize(), size)).iterator();
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            if (!iterator.hasNext()) {
                iterator = nextBatch();
            }
            current++;
            return iterator.next();
        }
    }
}
