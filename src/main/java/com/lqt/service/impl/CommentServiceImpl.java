package com.lqt.service.impl;

import com.lqt.dto.CommentDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Comment;
import com.lqt.pojo.Post;
import com.lqt.pojo.Role;
import com.lqt.repository.CommentRepository;
import com.lqt.repository.PostRepository;
import com.lqt.service.CommentService;
import com.lqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;

    @Override
    public CommentDto create(CommentDto commentDto, Long postId, Long userId, Long belongsCommentId) {
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Comment commentParent = null;
        if (belongsCommentId != null) {
            commentParent = commentRepository.findCommentById(belongsCommentId);
        }
        Comment comment;
        if (commentParent == null) {
            comment = Comment.builder()
                    .content(commentDto.getContent())
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .postId(post.getId())
                    .userId(userId)
                    .build();
        } else {
            comment = Comment.builder()
                    .content(commentDto.getContent())
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .postId(post.getId())
                    .belongsComment(commentParent)
                    .userId(userId)
                    .build();
        }
        Comment cmtResult = commentRepository.save(comment);

        CommentDto commentDto1 = CommentDto.builder()
                .belongsComment(cmtResult.getBelongsComment() != null ? cmtResult.getBelongsComment().getId() : null)
                .timestamp(cmtResult.getTimestamp())
                .postId(cmtResult.getPostId())
                .userId(cmtResult.getUserId())
                .content(cmtResult.getContent())
                .id(cmtResult.getId())
                .build();
        return commentDto1;
    }

    @Override
    public CommentDto update(CommentDto commentDto, Long postId, Long userId, Long commentId) {
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
        comment.setContent(commentDto.getContent());
        if (comment.getUserId() == userId) {
            Comment cmtResult = commentRepository.update(comment);
            CommentDto commentDto1 = CommentDto.builder()
                    .belongsComment(cmtResult.getBelongsComment().getId())
                    .timestamp(cmtResult.getTimestamp())
                    .postId(cmtResult.getPostId())
                    .userId(cmtResult.getUserId())
                    .content(cmtResult.getContent())
                    .id(cmtResult.getId())
                    .build();
            return commentDto1;
        } else {
            return null;
        }
    }

    @Override
    public Boolean delete(Long commentId, Long postId, Long userId) {
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole || comment.getUserId() == userId || post.getUserId() == userId) {
            return commentRepository.delete(comment);
        } else {
            return false;
        }
    }

    @Override
    public CommentDto findByCommentId(Long commentId) {
        Comment comment = commentRepository.findCommentById(commentId);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment", "id", commentId);
        }
        CommentDto commentDto = CommentDto.builder()
                .belongsComment(comment.getBelongsComment() == null ? null : comment.getBelongsComment().getId())
                .timestamp(comment.getTimestamp())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .id(comment.getId())
                .build();
        return commentDto;
    }

    @Override
    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        List<Comment> comments = commentRepository.findAllCommentsByPostId(postId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(c -> {
            CommentDto commentDto = CommentDto.builder()
                    .belongsComment(c.getBelongsComment() == null ? null : c.getBelongsComment().getId())
                    .postId(c.getPostId())
                    .timestamp(c.getTimestamp())
                    .userId(c.getUserId())
                    .content(c.getContent())
                    .id(c.getId())
                    .build();
            commentDtos.add(commentDto);
        });
        return commentDtos;
    }

    @Override
    public List<CommentDto> getAllCommentsByBelongsCommentId(Long postId, Long commentParentId) {
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Comment comment = commentRepository.findCommentById(commentParentId);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment", "id", commentParentId);
        }
        List<Comment> comments = commentRepository.findAllCommentsByCommentParentId(postId, commentParentId);
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(c -> {
            CommentDto commentDto = CommentDto.builder()
                    .belongsComment(c.getBelongsComment() == null ? null : c.getBelongsComment().getId())
                    .postId(c.getPostId())
                    .timestamp(c.getTimestamp())
                    .userId(c.getUserId())
                    .content(c.getContent())
                    .id(c.getId())
                    .build();
            commentDtos.add(commentDto);
        });
        return commentDtos;
    }
}
