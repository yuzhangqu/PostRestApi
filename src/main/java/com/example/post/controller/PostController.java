package com.example.post.controller;

import com.example.post.model.Comment;
import com.example.post.model.PageModel;
import com.example.post.model.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "/posts")
@RequestMapping(path = "/posts")
public class PostController {
    private final UserService userService;

    public PostController(UserService userService) {
        this.userService = userService;
    }

    private EntityModel<Post> toPostEntityModel(Post post) {
        EntityModel<Post> postModel = EntityModel.of(post, linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel());
        postModel.add(Link.of("/users/" + post.getAuthor(), "author").withType("GET"));
        postModel.add(Link.of("/posts/" + post.getId() + "/comments", "comments").withType("GET"));
        return postModel;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "根据ID获取文章")
    public ResponseEntity<EntityModel<Post>> getPost(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(toPostEntityModel(userService.getPost(id)));
    }

    @GetMapping
    @Operation(summary = "获取所有文章列表")
    public ResponseEntity<RepresentationModel<?>> getAllPosts(@PageableDefault(page = 1) Pageable pageable) {
        List<Post> posts = userService.getPosts(pageable.getPageNumber(), pageable.getPageSize());
        int total = userService.countPosts();
        List<EntityModel> postModelList = posts.stream().map(post -> EntityModel.of(linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(PostController.class).getAllPosts(pageable)).withSelfRel())
                .entity(new PageModel(postModelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "发布文章")
    public ResponseEntity<EntityModel<Post>> createPost(@RequestBody Post post) {
        if (!userService.hasUser(post.getAuthor())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.addPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(toPostEntityModel(post));
    }

    @GetMapping(path = "/{id}/comments")
    @Operation(summary = "获取文章的评论列表")
    public ResponseEntity<RepresentationModel<?>> getPostComments(@PathVariable String id, @PageableDefault(page = 1) Pageable pageable) {
        if (!userService.hasPost(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Comment> comments = userService.getPostComments(id, pageable.getPageNumber(), pageable.getPageSize());
        int total = Math.toIntExact(userService.countPostComments(id));
        List<EntityModel> commentModelList = comments.stream().map(comment -> EntityModel.of(linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(PostController.class).getPostComments(id, pageable)).withSelfRel())
                .entity(new PageModel(commentModelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }
}
