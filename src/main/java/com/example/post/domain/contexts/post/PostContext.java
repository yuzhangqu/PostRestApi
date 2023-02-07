package com.example.post.domain.contexts.post;

import com.example.post.domain.contexts.auth.User;

public interface PostContext {
    Poster asPoster(User user);
}
