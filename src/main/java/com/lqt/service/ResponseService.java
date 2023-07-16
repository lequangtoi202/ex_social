package com.lqt.service;

import com.lqt.pojo.Response;

import java.util.List;

public interface ResponseService {
    List<Response> getAllResponsesBySurveyId(Long surveyId);
    List<Response> getAllResponsesBySurveyAndQuestionId(Long surveyId, Long questionId);
    Response getResponseBySurveyAndQuestionIdAndByResId(Long surveyId, Long questionId, Long resId);
    Response createResponseForQuestion(Response res, Long questionId, Long surveyId);
    Response updateResponseForQuestion(Response res, Long questionId, Long surveyId, Long responseId);
    Boolean deleteResponseForQuestion(Long responseId);
}
