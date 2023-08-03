package com.lqt.service;

import com.lqt.dto.PostStatsResponse;
import com.lqt.dto.StatsUserResponse;

import java.util.List;
import java.util.Map;

public interface StatisticService {
    int countCommentOfPost(Long postId);
    int countInteractOfPost(Long postId);
    int countShareOfPost(Long postId);
    int countAllUsers();
    int countAllGroups();
    int countNumberOfUserInGroup(Long groupId);
    int countNumberOfPostsWithoutParam();
    List<PostStatsResponse> statsNumberOfPosts(Map<String, String> params);
    List<StatsUserResponse> statsUsers();
}
