package com.lqt.controllers;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.request.PasswordRequest;
import com.lqt.service.MailService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import com.lqt.util.Utility;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    //ok
    @PutMapping(Routing.ALUMNI_PROFILE)
    public ResponseEntity<AlumniResponse> updateProfileAlumni(@RequestBody AlumniRequest alumniRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            AlumniResponse alumniResponse = userService.updateProfileAlumni(alumniRequest, currentUser.getId());
            return ResponseEntity.ok(alumniResponse);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PutMapping(Routing.LECTURER_PROFILE)
    public ResponseEntity<LecturerResponse> updateProfileLecturer(@RequestBody LecturerRequest lecturerRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            LecturerResponse lecturerResponse = userService.updateProfileLecturer(lecturerRequest, currentUser.getId());
            return ResponseEntity.ok(lecturerResponse);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PutMapping(Routing.LECTURER_UPDATE_PASSWORD)
    public ResponseEntity<LecturerResponse> updatePasswordLecturer(@RequestBody PasswordRequest req) {
        String password = req.getPassword();
        String confirmPassword = req.getConfirmPassword();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            LecturerResponse lecturerResponse = userService.updatePasswordLecturer(currentUser.getId(), password, confirmPassword);
            return ResponseEntity.ok(lecturerResponse);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PostMapping(Routing.CHANGE_PASSWORD)
    public ResponseEntity<UserDto> changePasswordUser(@RequestBody PasswordRequest req) {
        String password = req.getPassword();
        String confirmPassword = req.getConfirmPassword();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            UserDto userDto = userService.changePassword(currentUser.getId(), password, confirmPassword);
            return ResponseEntity.ok(userDto);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    //ok
    @PostMapping(Routing.ASSIGN_ROLE)
    public ResponseEntity<String> assignRoleToUser(@RequestBody Role role, @PathVariable("id") Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            List<Role> roles = userService.getAllRoleOfUser(currentUser.getId());
            Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
            if (hasAdminRole) {
                Boolean rs = userService.assignRoleToUser(role, userId);
                if (rs) {
                    return ResponseEntity.ok("Assign role successfully!");
                }
                return ResponseEntity.badRequest().body("Assign role failed!");
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(Routing.RESET_LECTURER_UPDATE_PASSWORD)
    public ResponseEntity<LecturerResponse> resetTimeForLecturerUpdatePassword(@PathVariable("id") Long lecturerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            List<Role> roles = userService.getAllRoleOfUser(currentUser.getId());
            Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
            if (hasAdminRole) {
                LecturerResponse lecturerResponse = userService.resetWaitingChangePassword(lecturerId);
                return ResponseEntity.ok(lecturerResponse);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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

    //ok
    @GetMapping(Routing.MY_ACCOUNT)
    public ResponseEntity<?> getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.getMyAccount(userDetails.getUsername());
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .phone(user.getPhone())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .backgroundImage(user.getBackgroundImage())
                    .password(user.getPassword())
                    .avatarLink(user.getAvatar())
                    .build();
            return ResponseEntity.ok().body(userDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //ok
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @PostMapping(Routing.ALUMNI_CONFIRM)
    public ResponseEntity<String> confirmNewAlumni(@PathVariable("id") Long userId) {
        if (userService.confirmAlumni(userId)) {
            return ResponseEntity.ok("Confirm successfully!");
        }
        return ResponseEntity.badRequest().body("Server has error");
    }

    //ok
    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping(Routing.ALUMNI)
    public ResponseEntity<List<AlumniResponse>> getAllAlumniOrIsNotConfirmed(@RequestParam(value = "isNotConfirmed", required = false, defaultValue = "false") Boolean isNotConfirmed) {
        if (isNotConfirmed) {
            return ResponseEntity.ok(userService.getAllAlumniIsNotConfirmed());
        }
        return ResponseEntity.ok(userService.getAllAlumni());
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping(Routing.USERS)
    public ResponseEntity<List<UserDto>> getAllUser(@RequestParam(value = "name", required = false) String name) {
        if (name != null) {
            return ResponseEntity.ok(userService.findByFullName(name));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @GetMapping(Routing.LECTURER)
    public ResponseEntity<List<LecturerResponse>> getAllLecturers() {
        return ResponseEntity.ok(userService.getAllLecturers());
    }

    @PreAuthorize("hasRole('SYS_ADMIN')")
    @DeleteMapping(Routing.USER_BY_ID)
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            if (userService.deleteUser(id, currentUser.getId())) {
                return ResponseEntity.ok("Delete user successfully!");
            }
            return ResponseEntity.badRequest().body("Delete user failed!");
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    //ok
    @PostMapping(Routing.UPLOAD_BG_IMG)
    public ResponseEntity<String> uploadBackGroundImage(@RequestPart("bgImage") MultipartFile backgroundImage) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userService.getMyAccount(userDetails.getUsername());
            userService.uploadBgImage(backgroundImage, currentUser);
            return ResponseEntity.ok("Upload background image successfully!");
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
