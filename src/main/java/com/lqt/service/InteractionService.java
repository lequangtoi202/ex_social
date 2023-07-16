package com.lqt.service;

import com.lqt.pojo.Interaction;
import com.lqt.util.Action;

public interface InteractionService {
    Interaction interactWithPost(Long postId, Long userId, Action action);
}
