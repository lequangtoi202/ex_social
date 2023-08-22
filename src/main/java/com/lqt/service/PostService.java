package com.lqt.service;

import com.lqt.dto.PostDto;
import com.lqt.dto.UserDto;
import com.lqt.pojo.Post;
import com.lqt.pojo.User;

import java.util.List;

public interface PostService {
    PostDto post(PostDto postDto, Long userId);

    PostDto update(PostDto postDto, Long id, Long userId);

    Boolean delete(Long id, User u);

    PostDto lockPost(Long id, Long userId);

    PostDto unlockPost(Long id, Long userId);

    PostDto findPostById(Long id);

    List<PostDto> findAllPosts();

    List<PostDto> getAllMyPostsAndSharePosts(Long currentUserId, String orderDir);

    List<PostDto> findPostsByUserId(Long userId, String orderDir);

    List<PostDto> getAllPosts();
}
