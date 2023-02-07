package com.example.post.domain.contexts.auth.association.object;

import com.example.post.domain.contexts.auth.User;
import com.example.post.domain.contexts.post.PostContext;

public interface Users {
    User findUserByAccount(String account);

    PostContext inPostContext();
}
