package com.lqt.repository;

import java.util.Map;

public interface StatisticRepository {
    int countCommentOfPost(Long postId);
    int countInteractOfPost(Long postId);
    int countShareOfPost(Long postId);
    int countAllUsers();
    int countNumberOfUserInGroup(Long groupId);
    int countNumberOfPosts(Map<String, String> params);
}
