package com.lqt.repository;

import com.lqt.dto.PostStatsResponse;
import com.lqt.dto.StatsUserResponse;

import java.util.List;
import java.util.Map;

public interface StatisticRepository {
    int countCommentOfPost(Long postId);
    int countInteractOfPost(Long postId);
    int countShareOfPost(Long postId);
    int countAllUsers();
    int countAllGroups();
    int countNumberOfUserInGroup(Long groupId);
    int countNumberOfPosts();
    List<PostStatsResponse> statsNumberOfPosts(Map<String, String> params);
    List<StatsUserResponse> statsUser();
}
