package com.lqt.controllers;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Role;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.service.MailService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import com.lqt.util.Utility;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @PostMapping(Routing.ALUMNI_PROFILE)
    public ResponseEntity<AlumniResponse> updateProfileAlumni(@RequestBody AlumniRequest alumniRequest, @PathVariable("id") Long alumniId) {
        AlumniResponse alumniResponse = userService.updateProfileAlumni(alumniRequest, alumniId);
        return ResponseEntity.ok(alumniResponse);
    }

    @PostMapping(Routing.LECTURER_PROFILE)
    public ResponseEntity<LecturerResponse> updateProfileLecturer(@RequestBody LecturerRequest lecturerRequest, @PathVariable("id") Long lecturerId) {
        LecturerResponse lecturerResponse = userService.updateProfileLecturer(lecturerRequest, lecturerId);
        return ResponseEntity.ok(lecturerResponse);
    }

    @PostMapping(Routing.LECTURER_UPDATE_PASSWORD)
    public ResponseEntity<LecturerResponse> updatePasswordLecturer(HttpServletRequest req, @PathVariable("id") Long lecturerId) {
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        LecturerResponse lecturerResponse = userService.updatePasswordLecturer(lecturerId, password, confirmPassword);
        return ResponseEntity.ok(lecturerResponse);
    }

    @PostMapping(Routing.CHANGE_PASSWORD)
    public ResponseEntity<UserDto> changePasswordUser(HttpServletRequest req, @PathVariable("userId") Long userId) {
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        UserDto userDto = userService.changePassword(userId, password, confirmPassword);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(Routing.ASSIGN_ROLE)
    public ResponseEntity<String> assignRoleToUser(@RequestBody Role role, @PathVariable("id") Long userId) {
        Boolean rs = userService.assignRoleToUser(role, userId);
        if (rs) {
            return ResponseEntity.ok("Assign role successfully!");
        }
        return ResponseEntity.badRequest().body("Assign role failed!");
    }

    @GetMapping(Routing.RESET_LECTURER_UPDATE_PASSWORD)
    public ResponseEntity<LecturerResponse> resetTimeForLecturerUpdatePassword(@PathVariable("id") Long lecturerId) {
        LecturerResponse lecturerResponse = userService.resetWaitingChangePassword(lecturerId);
        return ResponseEntity.ok(lecturerResponse);
    }

    @PostMapping(Routing.FORGOT_PASSWORD)
    public ResponseEntity<String> executeForgotPassword(HttpServletRequest request, @RequestParam String email) {
        String token = RandomString.make(45);
        try {
            userService.updateResetPassword(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/api/v1/users/reset-password?token=" + token;

            mailService.sendMailForgotPassword(email, resetPasswordLink);
            return ResponseEntity.ok().body(resetPasswordLink);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Resource not found with email " + email);
        }
    }

    @GetMapping(Routing.RESET_PASSWORD)
    public ResponseEntity<String> checkTokenIsValid(@RequestParam String token) {
        UserDto userDto = userService.findByResetPasswordToken(token);
        if (userDto != null) {
            return ResponseEntity.ok().body("Token is valid!");
        }
        return ResponseEntity.badRequest().body("Token is invalid!");
    }

    @PostMapping(Routing.RESET_PASSWORD)
    public ResponseEntity<String> resetPassword(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        UserDto userDto = userService.findByResetPasswordToken(token);
        if (userDto != null) {
            userService.changePassword(userDto.getId(), password, confirmPassword);
            return ResponseEntity.ok().body("Reset password successfully!");
        }
        return ResponseEntity.badRequest().body("Token is invalid");
    }

    @GetMapping(Routing.MY_ACCOUNT)
    public ResponseEntity<?> getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok().body(userService.getMyAccount(userDetails.getUsername()));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
