package com.lqt.controllers;

import com.lqt.pojo.Option;
import com.lqt.service.OptionService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping(Routing.OPTIONS_BY_QUESTION_ID)
    public ResponseEntity<List<Option>> getAllOptionsOfQuestion(@PathVariable("id") Long questionId) {
        return ResponseEntity.ok(optionService.getAllOptionsOfQuestion(questionId));
    }
}
