package com.lqt.controllers;

import com.lqt.dto.SurveyDto;
import com.lqt.pojo.User;
import com.lqt.service.SurveyService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SurveyController {
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private UserService userService;

    //ok
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping(Routing.SURVEY)
    public ResponseEntity<List<SurveyDto>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    //ok
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping(Routing.SURVEY_OF_POST)
    public ResponseEntity<?> createSurvey(@PathVariable("postId") Long postId, @RequestBody SurveyDto surveyDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            SurveyDto surveys = surveyService.create(surveyDto, currentUser.getId(), postId);
            return new ResponseEntity<>(surveys == null ? new ResponseEntity<>("You do not have permission to access surveys", HttpStatus.UNAUTHORIZED) : surveys, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PutMapping(Routing.SURVEY_BY_ID)
    public ResponseEntity<?> updateSurvey(@RequestBody SurveyDto surveyDto, @PathVariable("surveyId") Long surveyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            SurveyDto surveys = surveyService.update(surveyDto, surveyId, currentUser.getId());
            return new ResponseEntity<>(surveys == null ? new ResponseEntity<>("You do not have permission to access surveys", HttpStatus.UNAUTHORIZED) : surveys, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(Routing.SURVEY_BY_ID)
    public ResponseEntity<?> deleteSurvey(@PathVariable("surveyId") Long surveyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = surveyService.delete(surveyId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.badRequest().body("You don not have permission to delete this survey");
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //ok
    @GetMapping(Routing.SURVEY_BY_ID)
    public ResponseEntity<SurveyDto> getSurveyById(@PathVariable("surveyId") Long surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyById(surveyId));
    }

    //ok
    @GetMapping(Routing.SURVEY_BY_USER_ID)
    public ResponseEntity<?> getSurveysByAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            List<SurveyDto> surveys = surveyService.getAllSurveysByUserId(currentUser.getId());
            return new ResponseEntity<>(surveys == null ? new ResponseEntity<>("You do not have permission to access surveys", HttpStatus.UNAUTHORIZED) : surveys, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
