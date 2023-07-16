package com.lqt.repository;

import com.lqt.dto.SurveyDto;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.pojo.Survey;

import java.util.List;

public interface SurveyRepository {
    Survey create(Survey survey);
    Survey update(Survey survey);
    Boolean delete(Survey survey);
    Survey findById(Long surveyId);
    List<Survey> getAllSurveys();
    List<Survey> getAllSurveysByUserId(Long userId);

    List<Survey> getAllSurveysByDate();//postpone
}
