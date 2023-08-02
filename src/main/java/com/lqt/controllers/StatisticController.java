package com.lqt.controllers;

import com.lqt.service.StatisticService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StatisticController {
    @Autowired
    private StatisticService statisticService;

    @GetMapping(Routing.COUNT_USER_OF_GROUP)
    public ResponseEntity<Integer> countNumberOfUserInGroup(@PathVariable("groupId") Long gropId) {
        return ResponseEntity.ok(statisticService.countNumberOfUserInGroup(gropId));
    }
}
