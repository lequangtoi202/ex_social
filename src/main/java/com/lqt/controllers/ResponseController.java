package com.lqt.controllers;

import com.lqt.pojo.Response;
import com.lqt.service.ResponseService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResponseController {
    @Autowired
    private ResponseService responseService;

    //ok
    @GetMapping(Routing.RESPONSE)
    public ResponseEntity<List<Response>> getAllResponsesBySurveyId(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(responseService.getAllResponsesBySurveyId(surveyId));
    }

    //ok
    @GetMapping(Routing.RESPONSE_BY_SURVEY_AND_QUESTION_ID)
    public ResponseEntity<List<Response>> getAllResponsesBySurveyIdAndQuestionId(@PathVariable("surveyId") Long surveyId, @PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok(responseService.getAllResponsesBySurveyAndQuestionId(surveyId, questionId));
    }

    //ok
    @PostMapping(Routing.RESPONSE_BY_SURVEY_AND_QUESTION_ID)
    public ResponseEntity<Response> createResponse(@RequestBody Response response, @PathVariable("surveyId") Long surveyId, @PathVariable("questionId") Long questionId) {
        return new ResponseEntity<>(responseService.createResponseForQuestion(response, questionId, surveyId), HttpStatus.CREATED);
    }

    //ok
    @GetMapping(Routing.RESPONSE_BY_SURVEY_AND_QUESTION_ID_AND_ID)
    public ResponseEntity<Response> getResponsesBySurveyIdAndQuestionIdAndById(@PathVariable("surveyId") Long surveyId, @PathVariable("questionId") Long questionId, @PathVariable("resId") Long resId) {
        return ResponseEntity.ok(responseService.getResponseBySurveyAndQuestionIdAndByResId(surveyId, questionId, resId));
    }


    @PutMapping(Routing.RESPONSE_BY_SURVEY_AND_QUESTION_ID_AND_ID)
    public ResponseEntity<Response> updateResponse(@RequestBody Response response, @PathVariable("surveyId") Long surveyId, @PathVariable("questionId") Long questionId, @PathVariable("resId") Long resId) {
        return ResponseEntity.ok(responseService.updateResponseForQuestion(response, questionId, surveyId, resId));
    }

    @DeleteMapping(Routing.RESPONSE_BY_SURVEY_AND_QUESTION_ID_AND_ID)
    public ResponseEntity<?> deleteResponse(@PathVariable("surveyId") Long surveyId, @PathVariable("questionId") Long questionId, @PathVariable("resId") Long resId) {
        if (responseService.deleteResponseForQuestion(surveyId, questionId, resId)) {
            return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.badRequest().body("Delete failed!");
    }
}
