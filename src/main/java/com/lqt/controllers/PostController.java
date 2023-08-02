package com.lqt.controllers;

import com.lqt.dto.PostDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.User;
import com.lqt.service.InteractionService;
import com.lqt.service.PostService;
import com.lqt.service.ShareService;
import com.lqt.service.UserService;
import com.lqt.util.Action;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShareService shareService;
    @Autowired
    private InteractionService interactionService;

    //ok
    @GetMapping(Routing.POST)
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.findAllPosts());
    }

    //ok
    @PostMapping(Routing.POST)
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            PostDto postDtoSaved = postService.post(postDto, currentUser.getId());
            return new ResponseEntity<>(postDtoSaved, HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //ok
    @PutMapping(Routing.POST_BY_ID)
    public ResponseEntity<?> updatePost(@RequestBody PostDto postDto, @PathVariable("id") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            PostDto postDtoSaved = postService.update(postDto, postId, currentUser.getId());
            return new ResponseEntity<>(postDtoSaved == null ? new ResponseEntity<>("You do not have permission to update this post", HttpStatus.UNAUTHORIZED) : postDtoSaved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping(Routing.POST_BY_ID)
    public ResponseEntity<?> deletePost(@PathVariable("id") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = postService.delete(postId, currentUser);
            if (rs) {
                return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.badRequest().body("You don not have permission to delete this post");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @GetMapping(Routing.POST_BY_ID)
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Long postId) {
        return ResponseEntity.ok(postService.findPostById(postId));
    }

    //ok
    @GetMapping(Routing.POST_BY_USER_ID)
    public ResponseEntity<List<PostDto>> getAllPostsByUserId(@PathVariable("userId") Long userId, @RequestParam(value = "order",defaultValue = "desc") String orderDir) {
        return ResponseEntity.ok(postService.findPostsByUserId(userId, orderDir));
    }

    //ok
    @PostMapping(Routing.LOCK_POST)
    public ResponseEntity<?> lockPost(@PathVariable("id") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            PostDto postDto = postService.lockPost(postId, currentUser.getId());
            return new ResponseEntity<>(postDto == null ? new ResponseEntity<>("You do not have permission to lock this post", HttpStatus.UNAUTHORIZED) : postDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PostMapping(Routing.SHARE_POST)
    public ResponseEntity<?> sharePost(@PathVariable("id") Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            shareService.sharePost(postId, currentUser.getId());
            return new ResponseEntity<>("Share post successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @PostMapping(Routing.INTERACT)
    public ResponseEntity<?> interactWithPost(@PathVariable("postId") Long postId, HttpServletRequest req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            String actionReq = req.getParameter("action");
            boolean exists = false;
            for (Action act : Action.values()) {
                if (act.name().equalsIgnoreCase(actionReq)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                Action action = Action.valueOf(actionReq.toUpperCase());
                interactionService.interactWithPost(postId, currentUser.getId(), action);
                return new ResponseEntity<>("Interact post successfully!", HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("Action", "name", actionReq);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
