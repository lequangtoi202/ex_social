package com.lqt.service;

import com.lqt.dto.CommentDto;
import com.lqt.dto.UserDto;

import java.util.List;

public interface CommentService {
    CommentDto create(CommentDto commentDto, Long postId, Long userId, Long belongsCommentId);
    CommentDto update(CommentDto commentDto, Long postId, Long userId, Long commentId);
    Boolean delete(Long commentId, Long postId, Long userId);
    CommentDto findByCommentId(Long commentId);
    List<CommentDto> getAllCommentsByPostId(Long postId);
    List<CommentDto> getAllCommentsByBelongsCommentId(Long postId, Long commentParentId);

}
