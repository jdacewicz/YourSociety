package pl.jdacewicz.sharingservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.CommentDto;
import pl.jdacewicz.sharingservice.dto.CommentRequest;
import pl.jdacewicz.sharingservice.dto.mapper.CommentMapper;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.service.CommentService;
import pl.jdacewicz.sharingservice.validation.ValidFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/comments",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    public CommentDto getVisibleCommentById(@PathVariable long id) {
        Comment comment = commentService.getVisibleCommentById(id);
        return commentMapper.convertToDto(comment);
    }

    @GetMapping("/post/{postId}")
    @PreAuthorize("hasRole('user')")
    public Page<CommentDto> getPostComments(@PathVariable long postId,
                                            @RequestParam(defaultValue = "true") boolean visible,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size,
                                            @RequestParam(defaultValue = "creationTime") String sort,
                                            @RequestParam(defaultValue = "ASC") String directory) {
        return commentService.getPostComments(postId, visible, page, size, sort, directory)
                .map(commentMapper::convertToDto);
    }

    @PostMapping(value = "/posts/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    public CommentDto commentPost(@AuthenticationPrincipal Jwt jwt,
                                  @PathVariable long postId,
                                  @Valid @RequestPart CommentRequest request,
                                  @ValidFile@RequestPart MultipartFile image) throws IOException {
        Comment comment = commentService.commentPost(jwt.getClaim("email"), postId, request, image);
        return commentMapper.convertToDto(comment);
    }

    @PostMapping(value = "/advertisements/{advertisementId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('user')")
    public CommentDto commentAdvertisement(@AuthenticationPrincipal Jwt jwt,
                                           @PathVariable int advertisementId,
                                           @Valid @RequestPart CommentRequest request,
                                           @ValidFile @RequestPart MultipartFile image) throws IOException {
        Comment comment = commentService.commentAdvertisement(jwt.getClaim("email"), advertisementId, request, image);
        return commentMapper.convertToDto(comment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public void changePostVisibility(@PathVariable long id,
                                     @RequestParam boolean visible) {
        commentService.changeCommentVisibility(id, visible);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public void deleteComment(@PathVariable long id) throws IOException {
        commentService.deleteComment(id);
    }
}
