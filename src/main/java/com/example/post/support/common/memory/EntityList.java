package com.example.post.support.common.memory;

import com.example.post.support.common.Many;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author yuzhangqu
 */
public class EntityList<T> implements Many<T> {
    private List<T> list = new ArrayList<>();

    public EntityList() {
    }

    public EntityList(List<T> list) {
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Many<T> subCollection(int from, int to) {
        return new EntityList<T>(this.list.subList(from, to));
    }

    @Override
    public Stream<T> stream() {
        return this.list.stream();
    }

    protected void addItem(T t){
        this.list.add(t);
    }
}
