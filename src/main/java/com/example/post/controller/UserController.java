package com.example.post.controller;

import com.example.post.model.PageModel;
import com.example.post.model.User;
import com.example.post.model.Post;
import com.example.post.persistent.mybatis.UserMapper;
import com.example.post.persistent.support.mybatis.IdHolder;
import com.example.post.view.PostVO;
import com.example.post.view.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author yuzhangqu
 */
@RestController
@Tag(name = "/users")
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private UserMapper userMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{account}")
    @Operation(summary = "获取单个用户")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable String account) {
        if (!userService.hasUser(account))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = userService.getUser(account);
        EntityModel<User> userModel = EntityModel.of(user, linkTo(methodOn(UserController.class).getUser(user.getAccount())).withSelfRel());
        userModel.add(Link.of("/users/" + user.getAccount() + "/posts", "posts").withType("GET"));

        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @GetMapping
    @Operation(summary = "获取用户列表")
    public ResponseEntity<RepresentationModel<?>> getAllUsers(@PageableDefault(page = 1) Pageable pageable) {
        List<User> users = userService.getUsers(pageable.getPageNumber(), pageable.getPageSize());
        int total = userService.countUsers();
        List<EntityModel> userModelList = users.stream().map(user -> EntityModel.of(linkTo(methodOn(UserController.class).getUser(user.getAccount())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(UserController.class).getAllUsers(pageable)).withSelfRel())
                .entity(new PageModel(userModelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "创建用户")
//    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
//        if (userService.hasUser(user.getAccount())) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//
//        userService.addUser(user);
//        EntityModel<User> userModel = EntityModel.of(user, linkTo(methodOn(UserController.class).getUser(user.getAccount())).withSelfRel());
//        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
//    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "创建用户")
    public ResponseEntity<EntityModel<UserVO>> createUser(@RequestBody UserVO userVO) {
        var user = userVO.toDomain();
        userMapper.insertUser(user);
        EntityModel<UserVO> userModel = EntityModel.of(userVO, linkTo(methodOn(UserController.class).getUser(userVO.getAccount())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @GetMapping(path = "/{account}/posts")
    @Operation(summary = "获取用户的文章列表")
    public ResponseEntity<RepresentationModel<?>> getUserPosts(@PathVariable String account, @PageableDefault(page = 1) Pageable pageable) {
        if (!userService.hasUser(account))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Post> posts = userService.getUserPosts(account, pageable.getPageNumber(), pageable.getPageSize());
        int total = Math.toIntExact(userService.countUserPosts(account));
        List<EntityModel> postModelList = posts.stream().map(post -> EntityModel.of(linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(UserController.class).getUserPosts(account, pageable)).withSelfRel())
                .entity(new PageModel(postModelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @PostMapping("/{account}/posts")
    @Operation(summary = "指定用户发布文章")
    public ResponseEntity<EntityModel<UserVO>> createUserPost(@PathVariable String account, @RequestBody PostVO postVO) {
        IdHolder idHolder = new IdHolder();
        userMapper.insertPost(idHolder, account, postVO.toDomain());
        var userVO = UserVO.fromDomain(userMapper.selectUser(account));
        EntityModel<UserVO> userModel = EntityModel.of(userVO, linkTo(methodOn(UserController.class).getUser(userVO.getAccount())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }
}
