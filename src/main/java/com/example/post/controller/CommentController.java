package com.example.post.controller;

import com.example.post.model.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author yuzhangqu
 */
@RestController
@Tag(name = "/comments")
@RequestMapping(path = "/comments")
public class CommentController {
    private final UserService userService;

    public CommentController(UserService userService) {
        this.userService = userService;
    }

    private EntityModel<Comment> toCommentEntityModel(Comment comment) {
        EntityModel<Comment> commentModel = EntityModel.of(comment, linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel());
        commentModel.add(Link.of("/users/" + comment.getAuthor(), "author").withType("GET"));
        commentModel.add(Link.of("/posts/" + comment.getPostId(), "post").withType("GET"));
        return commentModel;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "根据ID获取评论")
    public ResponseEntity<EntityModel<Comment>> getComment(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(toCommentEntityModel(userService.getComment(id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "发布评论")
    public ResponseEntity<EntityModel<Comment>> createComment(@RequestBody Comment comment) {
        if (!userService.hasUser(comment.getAuthor())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!userService.hasPost(comment.getPostId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        userService.addComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(toCommentEntityModel(comment));
    }
}
