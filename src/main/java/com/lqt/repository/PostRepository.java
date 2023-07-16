package com.lqt.repository;

import com.lqt.pojo.Post;

import java.util.List;

public interface PostRepository {
    Post post(Post post);
    Post update(Post post);
    Boolean delete(Post post);
    Post lockPost(Post post);
    Post findPostById(Long id);
    Post findPostByIdAndUserId(Long id, Long userId);
    List<Post> findAllPosts();
    List<Post> findPostsByUserId(Long userId);
}
