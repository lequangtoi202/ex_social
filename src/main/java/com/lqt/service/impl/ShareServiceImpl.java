package com.lqt.service.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Post;
import com.lqt.pojo.Share;
import com.lqt.pojo.User;
import com.lqt.repository.PostRepository;
import com.lqt.repository.ShareRepository;
import com.lqt.repository.UserRepository;
import com.lqt.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ShareServiceImpl implements ShareService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ShareRepository shareRepository;

    @Override
    public Share sharePost(Long postId, Long userId) {
        User u = userRepository.findById(userId);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Share share = Share.builder()
                .post(post)
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .userId(u.getId())
                .build();
        return shareRepository.sharePost(share);
    }
}
