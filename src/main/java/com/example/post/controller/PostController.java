package com.example.post.controller;

import com.example.post.model.Comment;
import com.example.post.model.PageModel;
import com.example.post.model.Post;
import com.example.post.view.PostVO;
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

import static java.lang.Math.max;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author yuzhangqu
 */
@RestController
@Tag(name = "/posts")
@RequestMapping(path = "/posts")
public class PostController {
    private final UserMapper userMapper;

    public PostController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private EntityModel<PostVO> toPostVOEntityModel(Post post) {
        var postVO = PostVO.fromDomain(post);
        var entityModel = EntityModel.of(postVO, linkTo(methodOn(PostController.class).getPost(postVO.getId())).withSelfRel());
        entityModel.add(Link.of("/users/" + postVO.getAuthor().getAccount(), "author").withType("GET"));
        entityModel.add(Link.of("/posts/" + postVO.getId() + "/comments", "comments").withType("GET"));
        return entityModel;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "根据ID获取文章")
    public ResponseEntity<EntityModel<PostVO>> getPost(@PathVariable Long id) {
        var post = userMapper.selectPost(id);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(toPostVOEntityModel(post));
    }

    @GetMapping
    @Operation(summary = "获取所有文章列表")
    public ResponseEntity<RepresentationModel<?>> getAllPosts(@PageableDefault(page = 1) Pageable pageable) {
        var posts = userMapper.selectPosts((max(1, pageable.getPageNumber()) - 1) * pageable.getPageSize(), pageable.getPageSize());
        int total = userMapper.countPosts();
        List<EntityModel> modelList = posts.stream().map(post -> EntityModel.of(linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(PostController.class).getAllPosts(pageable)).withSelfRel())
                .entity(new PageModel(modelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

//    @GetMapping(path = "/{id}/comments")
//    @Operation(summary = "获取文章的评论列表")
//    public ResponseEntity<RepresentationModel<?>> getPostComments(@PathVariable String id, @PageableDefault(page = 1) Pageable pageable) {
//        if (!userService.hasPost(id)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        List<Comment> comments = userService.getPostComments(id, pageable.getPageNumber(), pageable.getPageSize());
//        int total = Math.toIntExact(userService.countPostComments(id));
//        List<EntityModel> commentModelList = comments.stream().map(comment -> EntityModel.of(linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel())).collect(Collectors.toList());
//
//        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
//                .link(linkTo(methodOn(PostController.class).getPostComments(id, pageable)).withSelfRel())
//                .entity(new PageModel(commentModelList, pageable.getPageNumber(), pageable.getPageSize(), total))
//                .build());
//    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "发布评论")
//    public ResponseEntity<EntityModel<Comment>> createComment(@RequestBody Comment comment) {
//        if (!userService.hasUser(comment.getAuthor())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        if (!userService.hasPost(comment.getPostId())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        userService.addComment(comment);
//        return ResponseEntity.status(HttpStatus.CREATED).body(toCommentEntityModel(comment));
//    }
}
