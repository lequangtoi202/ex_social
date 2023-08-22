package com.lqt.service.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.repository.OptionRepository;
import com.lqt.repository.QuestionRepository;
import com.lqt.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {
    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Option> getAllOptionsOfQuestion(Long questionId) {
        Question question = questionRepository.getQuestionById(questionId);
        if (question == null) {
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        return optionRepository.getAllOptionsOfQuestion(questionId);
    }
}
