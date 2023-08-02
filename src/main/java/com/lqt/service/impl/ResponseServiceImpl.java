package com.lqt.service.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.pojo.Response;
import com.lqt.pojo.Survey;
import com.lqt.repository.QuestionRepository;
import com.lqt.repository.ResponseRepository;
import com.lqt.repository.SurveyRepository;
import com.lqt.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService {
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Response> getAllResponsesBySurveyId(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        return responseRepository.getAllResponsesBySurveyId(surveyId);
    }

    @Override
    public List<Response> getAllResponsesBySurveyAndQuestionId(Long surveyId, Long questionId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        return responseRepository.getAllResponsesBySurveyAndQuestionId(surveyId, questionId);
    }

    @Override
    public Response getResponseBySurveyAndQuestionIdAndByResId(Long surveyId, Long questionId, Long resId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        Response response = responseRepository.getByResponseId(resId);
        if (response == null){
            throw new ResourceNotFoundException("Response", "id", resId);
        }
        return responseRepository.getResponseBySurveyAndQuestionIdAndResId(surveyId, questionId, resId);
    }

    @Override
    public Response createResponseForQuestion(Response res, Long questionId, Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        Option option = responseRepository.getOptionById(res.getOption().getId());
        if (option == null){
            throw new ResourceNotFoundException("Option", "id", res.getOption().getId());
        }
        Response response = Response.builder()
                .content(res.getContent())
                .question(question)
                .surveyId(surveyId)
                .option(option)
                .build();
        return responseRepository.createResponseForQuestion(response);
    }

    @Override
    public Response updateResponseForQuestion(Response res, Long questionId, Long surveyId, Long responseId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        Response response = responseRepository.getByResponseId(responseId);
        if (response == null){
            throw new ResourceNotFoundException("Response", "id", responseId);
        }
        Option option = responseRepository.getOptionById(res.getOption().getId());
        if (option == null){
            throw new ResourceNotFoundException("Option", "id", res.getOption().getId());
        }
        Response responseSave = Response.builder()
                .id(response.getId())
                .content(res.getContent())
                .question(question)
                .surveyId(surveyId)
                .option(option)
                .build();
        return responseRepository.updateResponseForQuestion(responseSave);
    }

    @Override
    public Boolean deleteResponseForQuestion(Long surveyId, Long questionId, Long responseId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        Response response = responseRepository.getByResponseId(responseId);
        if (response == null){
            throw new ResourceNotFoundException("Response", "id", responseId);
        }
        return responseRepository.deleteResponseForQuestion(response);
    }
}
