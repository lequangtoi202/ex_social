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
import org.modelmapper.ModelMapper;
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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ModelMapper mapper;

    //ok
    @PutMapping(Routing.ALUMNI_PROFILE)
    public ResponseEntity<AlumniResponse> updateProfileAlumni(@RequestBody @Valid AlumniRequest alumniRequest) {
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
    public ResponseEntity<LecturerResponse> updateProfileLecturer(@RequestBody @Valid LecturerRequest lecturerRequest) {
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

    @GetMapping(Routing.IS_EXIST_EMAIL)
    public ResponseEntity<Boolean> isExistEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @GetMapping(Routing.IS_EXIST_USERNAME)
    public ResponseEntity<Boolean> isExistByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
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
                try {
                    Boolean rs = userService.assignRoleToUser(role, userId);
                    if (rs) {
                        return ResponseEntity.ok("Assign role successfully!");
                    } else {
                        return ResponseEntity.ok("Assign role failed!");
                    }
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
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

    @GetMapping(Routing.FORGOT_PASSWORD)
    public ResponseEntity<String> executeForgotPassword(HttpServletRequest request, @RequestParam("email") String email) {
        String token = RandomString.make(45);
        try {
            userService.updateResetPassword(token, email);
            String resetPasswordLink = "http://localhost:3000/reset-password?token=" + token;

            mailService.sendMailForgotPassword(email, resetPasswordLink);
            return ResponseEntity.ok().body(resetPasswordLink);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Resource not found with email " + email);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Send email has error " + email);
        }
    }

    @GetMapping(Routing.RESET_PASSWORD)
    public ResponseEntity<String> checkTokenIsValid(@RequestParam("token") String token) {
        UserDto userDto = userService.findByResetPasswordToken(token);
        if (userDto != null) {
            return ResponseEntity.ok().body("Token is valid!");
        }
        return ResponseEntity.badRequest().body("Token is invalid!");
    }

    @PostMapping(Routing.SEND_MAIL_ACCOUNT_TO_LECTURER)
    public ResponseEntity<String> sendMailAccountToLecturer(@RequestBody LecturerRequest lecturerRequest) {
        try {
            userService.sendMailAccountToLecturer(lecturerRequest.getEmail(), lecturerRequest.getUsername());
            return ResponseEntity.ok().body("Thông tin đã được gửi qua email của bạn.");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Token is invalid!");
        }
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
                    .displayName(user.getDisplayName())
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

    @GetMapping(Routing.USER_BY_USERNAME)
    public ResponseEntity<UserDto> getUserByUsername(@RequestParam(value = "username") String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(mapper.map(userService.findByUsername(username), UserDto.class));
        }
    }

    @GetMapping(Routing.USER_BY_EMAIL)
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam(value = "email") String email) {
        UserDto user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(user);
        }
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

    @GetMapping(Routing.USER_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
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
