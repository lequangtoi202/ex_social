package com.lqt.service;

import com.lqt.dto.PostReaction;
import com.lqt.pojo.Interaction;
import com.lqt.util.Action;

import java.util.List;

public interface InteractionService {
    Interaction interactWithPost(Long postId, Long userId, Action action);

    List<PostReaction> getAllPostReactionOfUser(Long userId);
}
