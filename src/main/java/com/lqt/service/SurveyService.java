package com.lqt.service;

import com.lqt.dto.SurveyDto;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;

import java.util.List;

public interface SurveyService {
    SurveyDto create(SurveyDto surveyDto, Long userId);
    SurveyDto update(SurveyDto surveyDto, Long surveyId, Long userId);
    Boolean delete(Long surveyId, Long userId);
    List<SurveyDto> getAllSurveys();
    SurveyDto getSurveyById(Long surveyId);
    List<SurveyDto> getAllSurveysByUserId(Long adminId);
    List<SurveyDto> getAllSurveysByDate();
}
