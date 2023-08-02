package com.lqt.service;

import java.util.Map;

public interface StatisticService {
    int countCommentOfPost(Long postId);
    int countInteractOfPost(Long postId);
    int countShareOfPost(Long postId);
    int countAllUsers();
    int countNumberOfUserInGroup(Long groupId);
    int countNumberOfPosts(Map<String, Integer> params);
}
