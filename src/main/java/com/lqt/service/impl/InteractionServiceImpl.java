package com.lqt.service.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Interaction;
import com.lqt.pojo.Post;
import com.lqt.pojo.User;
import com.lqt.repository.InteractionRepository;
import com.lqt.repository.PostRepository;
import com.lqt.repository.UserRepository;
import com.lqt.service.InteractionService;
import com.lqt.util.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class InteractionServiceImpl implements InteractionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private InteractionRepository interactionRepository;


    @Override
    public Interaction interactWithPost(Long postId, Long userId, Action action) {
        User u = userRepository.findById(userId);
        if (u == null){
            throw new ResourceNotFoundException("User", "id", userId);
        }
        Post post = postRepository.findPostById(postId);
        if (post == null){
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Interaction interaction = Interaction.builder()
                .userId(u.getId())
                .postId(post.getId())
                .reactionType(action.name())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return interactionRepository.interactWithPost(interaction);
    }
}
