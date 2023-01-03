package com.example.post.view;

import com.example.post.model.User;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuzhangqu
 */
@Data
public class UserVO {
    private String account;
    private String name;
    private List<PostVO> postList;

    public UserVO(String account, String name) {
        this.account = account;
        this.name = name;
    }

    public User toDomain() {
        User user = new User(this.account, this.name);
        return user;
    }

    public static UserVO fromDomain(User user){
        UserVO userVO = new UserVO(user.getAccount(), user.getName());
        if (user.getPostList() != null) {
            userVO.postList = user.getPostList().stream().map(PostVO::fromDomain).collect(Collectors.toList());
        }
        return userVO;
    }
}
