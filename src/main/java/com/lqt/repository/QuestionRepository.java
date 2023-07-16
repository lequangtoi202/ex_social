package com.lqt.repository;

import com.lqt.pojo.Option;
import com.lqt.pojo.Question;

import java.util.List;

public interface QuestionRepository {
    Question createQuestionForSurvey(Question question, List<Option> options);
    Question updateQuestionForSurvey(Question q);
    Boolean deleteQuestionForSurvey(Question q);
    Boolean deleteAllOptionOfQuestion(Long questionId);
    List<Question> getAllQuestionBySurveyId(Long surveyId);
    Question getQuestionById(Long questionId);
}
