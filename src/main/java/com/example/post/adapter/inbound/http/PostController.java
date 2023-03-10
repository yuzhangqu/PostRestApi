package com.example.post.adapter.inbound.http;

import com.example.post.adapter.outbound.persistent.mybatis.contexts.post.association.object.PostCommentsImpl;
import com.example.post.adapter.outbound.persistent.mybatis.mapper.UserMapper;
import com.example.post.adapter.inbound.http.support.PageModel;
import com.example.post.domain.contexts.post.Post;
import com.example.post.adapter.inbound.http.support.Pagination;
import com.example.post.adapter.inbound.http.representation.CommentVO;
import com.example.post.adapter.inbound.http.representation.PostVO;
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

    public static EntityModel<PostVO> toPostVOEntityModel(Post post) {
        var postVO = PostVO.fromDomain(post);
        var entityModel = EntityModel.of(postVO, linkTo(methodOn(PostController.class).getPost(postVO.getId())).withSelfRel());
        entityModel.add(Link.of("/users/" + postVO.getAuthor(), "author").withType("GET"));
        entityModel.add(Link.of("/posts/" + postVO.getId() + "/comments", "comments").withType("GET"));
        return entityModel;
    }

    @GetMapping(path = "/{postId}")
    @Operation(summary = "??????ID????????????")
    public ResponseEntity<EntityModel<PostVO>> getPost(@PathVariable Long postId) {
        var post = userMapper.selectPost(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(toPostVOEntityModel(post));
    }

    @GetMapping
    @Operation(summary = "????????????????????????")
    public ResponseEntity<RepresentationModel<?>> getAllPosts(@PageableDefault(page = 1) Pageable pageable) {
        var posts = userMapper.selectPosts((max(1, pageable.getPageNumber()) - 1) * pageable.getPageSize(), pageable.getPageSize());
        int total = userMapper.countPosts();
        List<EntityModel> modelList = posts.stream().map(post -> EntityModel.of(linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(PostController.class).getAllPosts(pageable)).withSelfRel())
                .entity(new PageModel(modelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @GetMapping(path = "/{postId}/comments")
    @Operation(summary = "???????????????????????????")
    public ResponseEntity<RepresentationModel<?>> getPostComments(@PathVariable Long postId, @PageableDefault(page = 1) Pageable pageable) {
        var post = userMapper.selectPost(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PostCommentsImpl postComments = (PostCommentsImpl) post.getPostComments();
        var pagination = new Pagination<>(pageable.getPageSize(), postComments.getAll());
        var page = pagination.page(max(1, pageable.getPageNumber()) - 1);
        int total = postComments.size();
        List<EntityModel> modelList = page.stream().map(comment -> EntityModel.of(linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel())).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(HalModelBuilder.emptyHalModel()
                .link(linkTo(methodOn(PostController.class).getPostComments(postId, pageable)).withSelfRel())
                .entity(new PageModel(modelList, pageable.getPageNumber(), pageable.getPageSize(), total))
                .build());
    }

    @PostMapping(path = "/{postId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "???????????????????????????")
    public ResponseEntity<EntityModel<CommentVO>> createComment(@PathVariable Long postId, @RequestBody CommentVO commentVO) {
        var post = userMapper.selectPost(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        var id = post.getPostComments().add(commentVO.toDomain());
        var comment = userMapper.selectComment(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentController.toCommentVOEntityModel(comment));
    }
}
