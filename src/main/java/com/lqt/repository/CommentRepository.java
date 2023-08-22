package com.lqt.repository;

import com.lqt.pojo.Comment;
import com.lqt.pojo.Post;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment update(Comment comment);

    Boolean delete(Comment comment);

    Comment findCommentById(Long id);

    List<Comment> findAllCommentsByPostId(Long postId);

    List<Comment> findAllCommentsByCommentParentId(Long postId, Long commentParentId);
}
