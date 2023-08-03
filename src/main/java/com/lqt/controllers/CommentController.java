package com.lqt.controllers;

import com.lqt.dto.CommentDto;
import com.lqt.pojo.User;
import com.lqt.service.CommentService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    //ok
    @GetMapping(Routing.COMMENT_BY_POST_ID)
    public ResponseEntity<List<CommentDto>> getAllCommentsByPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(commentService.getAllCommentsByPostId(postId));
    }

    //ok
    @GetMapping(Routing.COMMENT_BY_COMMENT_ID)
    public ResponseEntity<List<CommentDto>> getAllCommentsByParentComment(@PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        return ResponseEntity.ok(commentService.getAllCommentsByBelongsCommentId(postId, commentId));
    }

    //ok
    @GetMapping(Routing.COMMENT_BY_ID)
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("id") Long commentId) {
        return ResponseEntity.ok(commentService.findByCommentId(commentId));
    }

    //ok
    @PostMapping(Routing.COMMENT_BY_POST_ID)
    public ResponseEntity<?> createCommentToPost(@RequestBody @Valid CommentDto commentDto, @PathVariable("postId") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            CommentDto commentDtoSaved = commentService.create(commentDto, postId, currentUser.getId(), null);
            return new ResponseEntity<>(commentDtoSaved, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PostMapping(Routing.COMMENT_BY_COMMENT_ID)
    public ResponseEntity<?> replyCommentToComment(@RequestBody @Valid CommentDto commentDto, @PathVariable("postId") Long postId, @PathVariable("id") Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            CommentDto commentDtoSaved = commentService.create(commentDto, postId, currentUser.getId(), commentId);
            return new ResponseEntity<>(commentDtoSaved == null ? new ResponseEntity<>("You do not have permission to update this comment", HttpStatus.UNAUTHORIZED) : commentDtoSaved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PutMapping(Routing.COMMENT_BY_POST_AND_ID)
    public ResponseEntity<?> updateComment(@RequestBody @Valid CommentDto commentDto, @PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            CommentDto commentDtoSaved = commentService.update(commentDto, postId, currentUser.getId(), commentId);
            return new ResponseEntity<>(commentDtoSaved == null ? new ResponseEntity<>("You do not have permission to update this comment", HttpStatus.UNAUTHORIZED) : commentDtoSaved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping(Routing.COMMENT_BY_POST_AND_ID)
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long commentId, @PathVariable("postId") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = commentService.delete(commentId, postId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.badRequest().body("You don not have permission to delete this comment");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
