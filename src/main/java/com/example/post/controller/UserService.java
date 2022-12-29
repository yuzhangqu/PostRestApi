package com.example.post.controller;

import com.example.post.model.*;
import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final List<User> users;
    private static final List<Post> posts;
    private static final List<Comment> comments;

    static {
        users = new ArrayList<>();
        posts = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public void clear() {
        users.clear();
        posts.clear();
        comments.clear();
    }

    public boolean hasUser(String account) {
        return users.stream().anyMatch(user -> user.getAccount().equals(account));
    }

    public User getUser(String account) {
        return users.stream().filter(user -> user.getAccount().equals(account)).findFirst().get();
    }

    public void addUser(User user) {
        users.add(user);
    }

    private void adjustPageParam(MutableInt pageNum, MutableInt pageSize, int total) {
        if (pageSize.getValue() < 1) {
            pageSize.setValue(1);
        }

        int maxPage = 1;
        if (total > 0) {
            maxPage = total / pageSize.getValue() + (total % pageSize.getValue() == 0 ? 0 : 1);
        }

        if (pageNum.getValue() < 1) {
            pageNum.setValue(1);
        } else if (pageNum.getValue() > maxPage) {
            pageNum.setValue(maxPage);
        }
    }

    public List<User> getUsers(int pageNum, int pageSize) {
        MutableInt mutablePageNum = new MutableInt(pageNum);
        MutableInt mutablePageSize = new MutableInt(pageSize);
        adjustPageParam(mutablePageNum, mutablePageSize, users.size());
        pageNum = mutablePageNum.getValue();
        pageSize = mutablePageSize.getValue();
        return users.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public int countUsers() {
        return users.size();
    }

    public boolean hasPost(String id) {
        return posts.stream().anyMatch(post -> post.getId().equals(id));
    }

    public Post getPost(String id) {
        return posts.stream().filter(post -> post.getId().equals(id)).findFirst().get();
    }

    public void addPost(Post post) {
        post.setId(String.valueOf(posts.size() + 1));
        post.setTime(Instant.now());
        posts.add(post);
    }

    public List<Post> getPosts(int pageNum, int pageSize) {
        MutableInt mutablePageNum = new MutableInt(pageNum);
        MutableInt mutablePageSize = new MutableInt(pageSize);
        adjustPageParam(mutablePageNum, mutablePageSize, posts.size());
        pageNum = mutablePageNum.getValue();
        pageSize = mutablePageSize.getValue();

        return posts.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public int countPosts() {
        return posts.size();
    }

    public List<Post> getUserPosts(String account, int pageNum, int pageSize) {
        MutableInt mutablePageNum = new MutableInt(pageNum);
        MutableInt mutablePageSize = new MutableInt(pageSize);
        adjustPageParam(mutablePageNum, mutablePageSize, Math.toIntExact(countUserPosts(account)));
        pageNum = mutablePageNum.getValue();
        pageSize = mutablePageSize.getValue();

        return posts.stream().filter(post -> post.getAuthor().equals(account)).skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public long countUserPosts(String account) {
        return posts.stream().filter(post -> post.getAuthor().equals(account)).count();
    }

    public Comment getComment(String id) {
        return comments.stream().filter(comment -> comment.getId().equals(id)).findFirst().get();
    }

    public void addComment(Comment comment) {
        comment.setId(String.valueOf(comments.size() + 1));
        comment.setTime(Instant.now());
        comments.add(comment);
    }

    public List<Comment> getPostComments(String id, int pageNum, int pageSize) {
        MutableInt mutablePageNum = new MutableInt(pageNum);
        MutableInt mutablePageSize = new MutableInt(pageSize);
        adjustPageParam(mutablePageNum, mutablePageSize, Math.toIntExact(countPostComments(id)));
        pageNum = mutablePageNum.getValue();
        pageSize = mutablePageSize.getValue();

        return comments.stream().filter(comment -> comment.getPostId().equals(id)).skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }

    public long countPostComments(String id) {
        return comments.stream().filter(comment -> comment.getPostId().equals(id)).count();
    }
}
