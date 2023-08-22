package com.lqt.repository;

import com.lqt.dto.PostReaction;
import com.lqt.pojo.Interaction;

import java.util.List;

public interface InteractionRepository {
    Interaction interactWithPost(Interaction interaction);

    List<Interaction> countInteractWithPost(Long postId);

    List<PostReaction> getPostReactionOfUser(Long userId);
}
