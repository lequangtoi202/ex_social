package com.lqt.service;

import com.lqt.pojo.Option;

import java.util.List;

public interface OptionService {

    List<Option> getAllOptionsOfQuestion(Long questionId);
}
