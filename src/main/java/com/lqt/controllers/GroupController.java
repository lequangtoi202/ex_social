package com.lqt.controllers;

import com.lqt.pojo.Group;
import com.lqt.pojo.User;
import com.lqt.request.MailRequest;
import com.lqt.request.RequestMailToAlumni;
import com.lqt.service.GroupService;
import com.lqt.service.MailService;
import com.lqt.service.UserService;
import com.lqt.util.HTMLConverter;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.lqt.util.Constants.YYYY_MM_DD;

@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private JavaMailSender mailSender;
    //ok
    @GetMapping(Routing.GROUP)
    public ResponseEntity<List<Group>> getAllGroups(@RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(groupService.getAllGroups(name));
    }

    //ok
    @PostMapping(Routing.GROUP)
    public ResponseEntity<?> createGroup(@RequestBody @Valid Group group) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Group groupSaved = groupService.create(group, currentUser.getId());
            return new ResponseEntity<>(groupSaved == null ? new ResponseEntity<>("You do not have permission to update this group", HttpStatus.UNAUTHORIZED) : groupSaved, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //ok
    @PutMapping(Routing.GROUP_BY_ID)
    public ResponseEntity<?> updateGroup(@RequestBody @Valid Group group, @PathVariable("id") Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Group groupSaved = groupService.update(group, groupId, currentUser.getId());
            return new ResponseEntity<>(groupSaved == null ? new ResponseEntity<>("You do not have permission to update this group", HttpStatus.UNAUTHORIZED) : groupSaved, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping(Routing.GROUP_BY_ID)
    public ResponseEntity<?> deleteGroup(@PathVariable("id") Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = groupService.delete(groupId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Delete successfully!", HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    //ok
    @GetMapping(Routing.GROUP_BY_USER_ID)
    public ResponseEntity<List<Group>> getAllGroupsByCreator(@PathVariable("userId") Long creatorId) {
        return ResponseEntity.ok(groupService.getAllGroupsByUserId(creatorId));
    }

    //ok
    @GetMapping(Routing.GROUP_BY_ID)
    public ResponseEntity<Group> getGroupById(@PathVariable("id") Long groupId) {
        return ResponseEntity.ok(groupService.findById(groupId));
    }

    //ok
    @PostMapping(Routing.ADD_TO_GROUP)
    public ResponseEntity<?> addUserToGroup(@PathVariable("id") Long groupId, @PathVariable("userId") Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = groupService.addMemberToGroup(userId, groupId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Add user to group successfully!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //ok
    @DeleteMapping(Routing.DELETE_TO_GROUP)
    public ResponseEntity<?> deleteUserFromGroup(@PathVariable("id") Long groupId, @PathVariable("userId") Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            Boolean rs = groupService.deleteMemberFromGroup(userId, groupId, currentUser.getId());
            if (rs) {
                return new ResponseEntity<>("Delete user to group successfully!", HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //ok
    @PostMapping(Routing.MAIL_BY_GROUP_ID)
    public ResponseEntity<?> sendMailToGroup(@RequestBody RequestMailToAlumni requestMail, @PathVariable("id") Long groupId) {
        String subject = requestMail.getSubject();
        String body = HTMLConverter.convertToHTML(requestMail.getBody());
        String from = requestMail.getFrom();
        List<String> listMail = groupService.getAllMailOfGroup(groupId);
        listMail.stream().forEach(m -> {
            MailRequest mailRequest = MailRequest.builder()
                    .date(LocalDate.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD)))
                    .body(body)
                    .subject(subject)
                    .from(from)
                    .recipients(m)
                    .build();
            mailService.sendMailToEachPersonGroup(mailRequest);
        });
        return ResponseEntity.ok("Send mail to group successfully!");
    }

    //ok
    @PostMapping(Routing.MAIL)
    public ResponseEntity<?> sendMailToAllMembersInGroups(@RequestBody RequestMailToAlumni requestMail) {
        String subject = requestMail.getSubject();
        String body = HTMLConverter.convertToHTML(requestMail.getBody());
        String from = requestMail.getFrom();
        List<String> listMail = groupService.getAllEmailOfAlumni();
        listMail.stream().forEach(m -> {
            MailRequest mailRequest = MailRequest.builder()
                    .date(LocalDate.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD)))
                    .body(body)
                    .subject(subject)
                    .from(from)
                    .recipients(m)
                    .build();
            mailService.sendMailToEachPersonGroup(mailRequest);
        });
        return ResponseEntity.ok("Send mail to all member successfully!");
    }

}
