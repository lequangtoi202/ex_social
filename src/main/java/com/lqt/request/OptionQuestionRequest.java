package com.lqt.request;

import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OptionQuestionRequest {
    private Question question;
    private List<Option> options;
}
