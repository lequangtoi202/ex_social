package com.lqt.repository;

import com.lqt.pojo.Option;

import java.util.List;

public interface OptionRepository {
    List<Option> getAllOptionsOfQuestion(Long questionId);
}
