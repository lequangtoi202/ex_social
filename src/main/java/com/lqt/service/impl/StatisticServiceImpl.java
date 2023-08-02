package com.lqt.service.impl;

import com.lqt.repository.*;
import com.lqt.service.PostService;
import com.lqt.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public int countCommentOfPost(Long postId) {
        return 0;
    }

    @Override
    public int countInteractOfPost(Long postId) {
        return 0;
    }

    @Override
    public int countShareOfPost(Long postId) {
        return 0;
    }

    @Override
    public int countAllUsers() {
        return 0;
    }

    @Override
    public int countNumberOfUserInGroup(Long groupId) {
        return 0;
    }

    @Override
    public int countNumberOfPosts(Map<String, Integer> params) {
        return 0;
    }
}
