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
    private final UserMapper userMapper;

    public CommentController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

//    private EntityModel<Comment> toCommentEntityModel(Comment comment) {
//        EntityModel<Comment> commentModel = EntityModel.of(comment, linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel());
//        commentModel.add(Link.of("/users/" + comment.getAuthor(), "author").withType("GET"));
//        commentModel.add(Link.of("/posts/" + comment.getPostId(), "post").withType("GET"));
//        return commentModel;
//    }
//
//    @GetMapping(path = "/{id}")
//    @Operation(summary = "根据ID获取评论")
//    public ResponseEntity<EntityModel<Comment>> getComment(@PathVariable String id) {
//        return ResponseEntity.status(HttpStatus.OK).body(toCommentEntityModel(userService.getComment(id)));
//    }
}
