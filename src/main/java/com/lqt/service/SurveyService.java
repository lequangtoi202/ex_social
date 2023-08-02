package com.lqt.service;

import com.lqt.dto.SurveyDto;

import java.util.List;

public interface SurveyService {
    SurveyDto create(SurveyDto surveyDto, Long userId, Long postId);

    SurveyDto update(SurveyDto surveyDto, Long surveyId, Long userId);

    Boolean delete(Long surveyId, Long userId);

    List<SurveyDto> getAllSurveys();

    SurveyDto getSurveyById(Long surveyId);

    List<SurveyDto> getAllSurveysByUserId(Long adminId);

    List<SurveyDto> getAllSurveysByDate();
}
