package com.lqt.controllers;

import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.pojo.User;
import com.lqt.request.OptionQuestionRequest;
import com.lqt.service.QuestionService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    //ok
    @GetMapping(Routing.QUESTION_BY_SURVEY)
    public ResponseEntity<?> getAllQuestionsBySurveyId(@PathVariable("surveyId") Long surveyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            List<Question> questions = questionService.getAllQuestionsBySurveyId(surveyId, currentUser.getId());
            return new ResponseEntity<>(questions == null ? new ResponseEntity<>("You do not have permission to access questions", HttpStatus.UNAUTHORIZED) : questions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //ok
    @PostMapping(Routing.QUESTION_BY_SURVEY)
    public ResponseEntity<?> createQuestion(@RequestBody OptionQuestionRequest optionQuestionRequest, @PathVariable("surveyId") Long surveyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Question question = optionQuestionRequest.getQuestion();
            List<Option> options = optionQuestionRequest.getOptions();
            Question questionSaved = questionService.createQuestionForSurvey(surveyId, question, options, currentUser.getId());
            return new ResponseEntity<>(questionSaved == null ? new ResponseEntity<>("You do not have permission to access question", HttpStatus.UNAUTHORIZED) : questionSaved, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @PutMapping(Routing.QUESTION_BY_ID)
    public ResponseEntity<?> updateQuestion(@RequestBody Question question, @PathVariable("id") Long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Question questionSaved = questionService.updateQuestionForSurvey(question, questionId, currentUser.getId());
            return new ResponseEntity<>(questionSaved == null ? new ResponseEntity<>("You do not have permission to access question", HttpStatus.UNAUTHORIZED) : questionSaved, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(Routing.QUESTION_BY_ID)
    public ResponseEntity<?> deleteQuestionById(@PathVariable("id") Long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = questionService.deleteQuestionForSurvey(questionId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.badRequest().body("You don not have permission to delete this question");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @GetMapping(Routing.QUESTION_BY_ID)
    public ResponseEntity<?> getQuestionByQuestionId(@PathVariable("id") Long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Question question = questionService.getQuestionById(questionId, currentUser.getId());
            return new ResponseEntity<>(question == null ? new ResponseEntity<>("You do not have permission to access question", HttpStatus.UNAUTHORIZED) : question, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
