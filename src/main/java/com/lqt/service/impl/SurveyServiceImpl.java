package com.lqt.service.impl;

import com.lqt.dto.SurveyDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Post;
import com.lqt.pojo.Role;
import com.lqt.pojo.Survey;
import com.lqt.pojo.User;
import com.lqt.repository.PostRepository;
import com.lqt.repository.SurveyRepository;
import com.lqt.repository.UserRepository;
import com.lqt.service.SurveyService;
import com.lqt.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper mapper;


    @Override
    public SurveyDto create(SurveyDto surveyDto, Long userId, Long postId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Post post = postRepository.findPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }
        Survey survey = Survey.builder()
                .createdOn(Timestamp.valueOf(LocalDateTime.now()))
                .description(surveyDto.getDescription())
                .title(surveyDto.getTitle())
                .type(surveyDto.getType())
                .userId(user.getId())
                .post(post)
                .build();
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole) {
            return mapper.map(surveyRepository.create(survey), SurveyDto.class);
        } else {
            return null;
        }
    }

    @Override
    public SurveyDto update(SurveyDto surveyDto, Long surveyId, Long userId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null) {
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        survey.setDescription(surveyDto.getDescription());
        survey.setTitle(surveyDto.getTitle());
        survey.setType(surveyDto.getType());
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole) {
            return mapper.map(surveyRepository.update(survey), SurveyDto.class);
        } else {
            return null;
        }
    }

    @Override
    public Boolean delete(Long surveyId, Long userId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null) {
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        List<Role> roles = userService.getAllRoleOfUser(userId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole) {
            return surveyRepository.delete(survey);
        } else {
            return false;
        }

    }

    @Override
    public List<SurveyDto> getAllSurveys() {
        return surveyRepository.getAllSurveys().stream()
                .map(s -> mapper.map(s, SurveyDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public SurveyDto getSurveyById(Long surveyId) {
        return mapper.map(surveyRepository.findById(surveyId), SurveyDto.class);
    }

    @Override
    public List<SurveyDto> getAllSurveysByUserId(Long adminId) {
        User user = userRepository.findById(adminId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", adminId);
        }
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole) {
            return surveyRepository.getAllSurveysByUserId(user.getId()).stream()
                    .map(s -> mapper.map(s, SurveyDto.class))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public List<SurveyDto> getAllSurveysByDate() {
        return null;
    }
}
