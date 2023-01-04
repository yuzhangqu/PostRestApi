package com.example.post.support.common.database;

import com.example.post.support.common.HasMany;
import com.example.post.support.common.Many;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author yuzhangqu
 */
public abstract class EntityList<ID, T> implements Many<T>, HasMany<ID, T> {
    protected abstract T findEntity(ID id);
    protected abstract List<T> findEntities(int from, int to);

    @Override
    public Many<T> subCollection(int from, int to) {
        return new com.example.post.support.common.memory.EntityList<>(findEntities(from, to));
    }

    @Override
    public final Many<T> findAll() {
        return this;
    }

    @Override
    public final Optional<T> findById(ID id) {
        return Optional.ofNullable(findEntity(id));
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
