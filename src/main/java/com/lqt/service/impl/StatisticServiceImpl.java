package com.lqt.service.impl;

import com.lqt.dto.PostStatsResponse;
import com.lqt.dto.StatsUserResponse;
import com.lqt.pojo.*;
import com.lqt.repository.*;
import com.lqt.service.PostService;
import com.lqt.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private StatisticRepository statisticRepository;

    @Override
    public int countCommentOfPost(Long postId) {
        return statisticRepository.countCommentOfPost(postId);
    }

    @Override
    public int countInteractOfPost(Long postId) {
        return statisticRepository.countInteractOfPost(postId);
    }

    @Override
    public int countShareOfPost(Long postId) {
        return statisticRepository.countShareOfPost(postId);
    }

    @Override
    public int countAllUsers() {
        return statisticRepository.countAllUsers();
    }

    @Override
    public int countAllGroups() {
        return statisticRepository.countAllGroups();
    }

    @Override
    public int countNumberOfUserInGroup(Long groupId) {
        return statisticRepository.countNumberOfUserInGroup(groupId);
    }

    @Override
    public int countNumberOfPostsWithoutParam() {
        return statisticRepository.countNumberOfPosts();
    }

    @Override
    public List<PostStatsResponse> statsNumberOfPosts(Map<String, String> params) {
        return statisticRepository.statsNumberOfPosts(params);
    }

    @Override
    public List<StatsUserResponse> statsUsers() {
        return statisticRepository.statsUser();
    }
}
