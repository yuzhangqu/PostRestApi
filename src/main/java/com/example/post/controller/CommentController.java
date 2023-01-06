package com.example.post.controller;

import com.example.post.model.Comment;
import com.example.post.view.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
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

    public static EntityModel<CommentVO> toCommentVOEntityModel(Comment comment) {
        var commentVO = CommentVO.fromDomain(comment);
        var entityModel = EntityModel.of(commentVO, linkTo(methodOn(CommentController.class).getComment(commentVO.getId())).withSelfRel());
        entityModel.add(Link.of("/posts/" + commentVO.getPost().getId(), "post").withType("GET"));
        return entityModel;
    }

    @GetMapping(path = "/{id}")
    @Operation(summary = "根据ID获取评论")
    public ResponseEntity<EntityModel<CommentVO>> getComment(@PathVariable Long id) {
        var comment = userMapper.selectComment(id);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(toCommentVOEntityModel(comment));
    }
}
