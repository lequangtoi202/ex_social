package com.lqt.service.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.pojo.Role;
import com.lqt.pojo.Survey;
import com.lqt.repository.QuestionRepository;
import com.lqt.repository.SurveyRepository;
import com.lqt.service.QuestionService;
import com.lqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserService userService;

    @Override
    public Question createQuestionForSurvey(Long surveyId, Question q, List<Option> options, Long adminId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        Question question = Question.builder()
                .content(q.getContent())
                .surveyId(survey.getId())
                .build();
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return questionRepository.createQuestionForSurvey(question, options);
        }else{
            return null;
        }
    }

    @Override
    public Question updateQuestionForSurvey(Question q, Long questionId, Long adminId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        question.setContent(q.getContent());
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return questionRepository.updateQuestionForSurvey(question);
        }else{
            return null;
        }
    }

    @Override
    public Boolean deleteQuestionForSurvey(Long questionId, Long adminId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return questionRepository.deleteQuestionForSurvey(question);
        }else{
            return null;
        }
    }
//
//    @Override
//    public Boolean deleteAllOptionOfQuestion(Long questionId, Long adminId) {
//        return questionRepository.deleteAllOptionOfQuestion(questionId);
//    }

    @Override
    public List<Question> getAllQuestionsBySurveyId(Long surveyId, Long adminId) {
        Survey survey = surveyRepository.findById(surveyId);
        if (survey == null){
            throw new ResourceNotFoundException("Survey", "id", surveyId);
        }
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return questionRepository.getAllQuestionBySurveyId(surveyId);
        }else{
            return null;
        }
    }

    @Override
    public Question getQuestionById(Long questionId, Long adminId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null){
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        List<Role> roles = userService.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole){
            return questionRepository.getQuestionById(questionId);
        }else{
            return null;
        }
    }
}
