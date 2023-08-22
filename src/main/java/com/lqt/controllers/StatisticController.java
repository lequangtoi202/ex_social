package com.lqt.controllers;

import com.lqt.service.StatisticService;
import com.lqt.service.UserService;
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

    @GetMapping(Routing.COUNT_USER_OF_SYSTEM)
    public ResponseEntity<Integer> countNumberOfUserInSystem() {
        return ResponseEntity.ok(statisticService.countAllUsers());
    }

    @GetMapping(Routing.COUNT_REACT_OF_POST)
    public ResponseEntity<Integer> countNumOfReact(@PathVariable("postId") long id) {
        return ResponseEntity.ok(statisticService.countInteractOfPost(id));
    }

    @GetMapping(Routing.COUNT_COMMENT_OF_POST)
    public ResponseEntity<Integer> countNumOfComment(@PathVariable("postId") long id) {
        return ResponseEntity.ok(statisticService.countCommentOfPost(id));
    }

    @GetMapping(Routing.COUNT_SHARE_OF_POST)
    public ResponseEntity<Integer> countNumOfShare(@PathVariable("postId") long id) {
        return ResponseEntity.ok(statisticService.countShareOfPost(id));
    }


}
