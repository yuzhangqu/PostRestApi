package com.example.post.persistent.mybatis;

import com.example.post.model.*;
import com.example.post.persistent.support.mybatis.IdHolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author yuzhangqu
 */
@Mapper
public interface UserMapper {
    void insertUser(@Param("user") User user);
    void insertUserPost(@Param("holder") IdHolder id, @Param("account") String account, @Param("post") Post post);
    User findUserById(@Param("account") String account);
}
