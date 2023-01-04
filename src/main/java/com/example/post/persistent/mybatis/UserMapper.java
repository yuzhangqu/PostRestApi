package com.example.post.persistent.mybatis;

import com.example.post.model.*;
import com.example.post.persistent.support.mybatis.IdHolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuzhangqu
 */
@Mapper
public interface UserMapper {
    void insertUser(@Param("user") User user);
    void insertPost(@Param("holder") IdHolder id, String account, @Param("post") Post post);
    User selectUser(String account);
    List<Post> selectPostsByAuthor(String account, int from, int size);
}
