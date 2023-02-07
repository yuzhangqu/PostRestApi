package com.example.post.adapter.inbound.http;

import com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object.UserPostsImpl;
import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.adapter.inbound.http.support.PageModel;
import com.example.post.adapter.inbound.http.support.Pagination;
import com.example.post.adapter.inbound.http.representation.PostVO;
import com.example.post.adapter.inbound.http.representation.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DuplicateKeyException;
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

import static java.lang.Math.max;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author yuzhangqu
 */
@RestController
@Tag(name = "/users")
@RequestMapping(path = "/users")
public class UserController {
    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping(path = "/{account}")
    @Operation(summary = "获取单个用户")
    public ResponseEntity<EntityModel<UserVO>> getUser(@PathVariable String account) {
        var user = userMapper.selectUser(account);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        var userVO = UserVO.fromDomain(user);
        var entityModel = EntityModel.of(userVO, linkTo(methodOn(UserController.class).getUser(userVO.getAccount())).withSelfRel());
        entityModel.add(Link.of("/users/" + user.getAccount() + "/posts", "posts").withType("GET"));

        return ResponseEntity.status(HttpStatus.OK).body(entityModel);
    }

    @GetMapping
    @Operation(summary = "获取用户列表")
    public ResponseEntity<RepresentationModel<?>> getAllUsers(@PageableDefault(page = 1) Pageable pageable) {
        var users = userMapper.selectUsers((max(1, pageable.getPageNumber()) - 1) * pageable.getPageSize(), pageable.getPageSize());
        int total = userMapper.countUsers();
        List<EntityModel> modelList = users.stream().map(user -> EntityModel.of(linkTo(methodOn(UserController.class).getUser(user.getAccount())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(UserController.class).getAllUsers(pageable)).withSelfRel())
                .entity(new PageModel(modelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "创建用户")
    public ResponseEntity<EntityModel<UserVO>> createUser(@RequestBody UserVO userVO) {
        try {
            userMapper.insertUser(userVO.toDomain());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        var entityModel = EntityModel.of(userVO, linkTo(methodOn(UserController.class).getUser(userVO.getAccount())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @GetMapping(path = "/{account}/posts")
    @Operation(summary = "获取用户的文章列表")
    public ResponseEntity<RepresentationModel<?>> getUserPosts(@PathVariable String account, @PageableDefault(page = 1) Pageable pageable) {
        var poster = userMapper.selectPoster(account);
        if (poster == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        UserPostsImpl userPosts = (UserPostsImpl) poster.getUserPosts();
        var pagination = new Pagination<>(pageable.getPageSize(), userPosts.getAll());
        var page = pagination.page(max(1, pageable.getPageNumber()) - 1);
        int total = userPosts.size();
        List<EntityModel> modelList = page.stream().map(post -> EntityModel.of(linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(UserController.class).getUserPosts(account, pageable)).withSelfRel())
                .entity(new PageModel(modelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @PostMapping(path = "/{account}/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "指定用户发布文章")
    public ResponseEntity<EntityModel<PostVO>> createUserPost(@PathVariable String account, @RequestBody PostVO postVO) {
        var poster = userMapper.selectPoster(account);
        if (poster == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var id = poster.getUserPosts().add(postVO.toDomain());
        var post = userMapper.selectPost(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(PostController.toPostVOEntityModel(post));
    }
}
