package com.lqt.service;

import com.lqt.pojo.Option;
import com.lqt.pojo.Question;

import java.util.List;

public interface QuestionService {
    Question createQuestionForSurvey(Long surveyId, Question q, List<Option> options, Long adminId);

    Question updateQuestionForSurvey(Question q, Long questionId, Long adminId);

    Boolean deleteQuestionForSurvey(Long questionId, Long adminId);

    //Boolean deleteAllOptionOfQuestion(Long questionId, Long adminId);
    List<Question> getAllQuestionsBySurveyId(Long surveyId, Long adminId);

    Question getQuestionById(Long questionId, Long adminId);
}
