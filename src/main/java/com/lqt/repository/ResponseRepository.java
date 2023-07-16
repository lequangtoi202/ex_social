package com.lqt.repository;

import com.lqt.pojo.Option;
import com.lqt.pojo.Response;

import java.util.List;

public interface ResponseRepository {
    List<Response> getAllResponsesBySurveyId(Long surveyId);
    List<Response> getAllResponsesBySurveyAndQuestionId(Long surveyId, Long questionId);
    Response getResponseBySurveyAndQuestionIdAndResId(Long surveyId, Long questionId, Long resId);
    Response getByResponseId(Long resId);
    Response createResponseForQuestion(Response res);
    Response updateResponseForQuestion(Response res);
    Boolean deleteResponseForQuestion(Response res);
    Option getOptionById(Long optId);
}
