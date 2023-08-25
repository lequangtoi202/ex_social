package com.lqt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lqt.dto.*;
import com.lqt.pojo.Alumni;
import com.lqt.pojo.Lecturer;
import com.lqt.pojo.User;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.service.AuthService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userDetailServiceImpl;

    @PostMapping(Routing.LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) throws Exception {
        try {
            authenticate(loginDto.getUsername(), loginDto.getPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Username or password is invalid!");
        }

        final UserDetails userDetails = userDetailServiceImpl
                .loadUserByUsername(loginDto.getUsername());
        User user = userDetailServiceImpl.findByUsername(userDetails.getUsername());
        Lecturer lecturer = userDetailServiceImpl.findLecturerByUserId(user.getId());
        Alumni alumni = userDetailServiceImpl.findAlumniByUserId(user.getId());
        if (alumni != null) {
            Boolean isConfirmed = userDetailServiceImpl.checkAlumniIsConfirmed(alumni);
            if (isConfirmed) {
                JwtResponse jwtResponse = authService.login(userDetails);
                if (jwtResponse != null) {
                    Cookie cookie = new Cookie("JWT_TOKEN", jwtResponse.getAccessToken());
                    cookie.setPath("/");
                    cookie.setMaxAge(3600);

                    response.addCookie(cookie);
                    return ResponseEntity.ok().body(jwtResponse);
                } else {
                    return ResponseEntity.badRequest().body("Username or password is invalid!");
                }
            } else {
                return new ResponseEntity<>("Your account is not confirmed by admin. Please waiting!..", HttpStatus.BAD_REQUEST);
            }
        } else if (lecturer != null) {
            Boolean isLocked = userDetailServiceImpl.checkLecturerIsLocked(lecturer);
            if (!isLocked) {
                JwtResponse jwtResponse = authService.login(userDetails);
                if (jwtResponse != null) {
                    Cookie cookie = new Cookie("JWT_TOKEN", jwtResponse.getAccessToken());
                    cookie.setPath("/");
                    cookie.setMaxAge(3600);

                    response.addCookie(cookie);
                    return ResponseEntity.ok().body(jwtResponse);
                } else {
                    return ResponseEntity.badRequest().body("Username or password is invalid!");
                }
            } else {
                return new ResponseEntity<>("Your account is locked. Please contact admin!", HttpStatus.BAD_REQUEST);
            }
        } else {
            JwtResponse jwtResponse = authService.login(userDetails);
            if (jwtResponse != null) {
                Cookie cookie = new Cookie("JWT_TOKEN", jwtResponse.getAccessToken());
                cookie.setPath("/");
                cookie.setMaxAge(3600);

                response.addCookie(cookie);
                return ResponseEntity.ok().body(jwtResponse);
            } else {
                return ResponseEntity.badRequest().body("Username or password is invalid!");
            }
        }
    }

    private void authenticate(String username, String password) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PostMapping(value = Routing.ADMIN_REGISTER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> adminRegister(@RequestParam("userDto") String userRequest, @RequestPart("avatar") MultipartFile avatar) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserDto userDto = objectMapper.readValue(userRequest, UserDto.class);
            UserDto userDtoResponse = authService.userRegister(userDto, avatar);
            return new ResponseEntity<>(userDtoResponse, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = Routing.ALUMNI_REGISTER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlumniResponse> alumniRegister(@RequestParam("alumni") String alumniRequest, @RequestPart MultipartFile avatar) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AlumniRequest alumni = objectMapper.readValue(alumniRequest, AlumniRequest.class);
            AlumniResponse alumniResponse = authService.alumniRegister(alumni, avatar);
            return new ResponseEntity<>(alumniResponse, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(value = Routing.LECTURER_REGISTER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LecturerResponse> lecturerRegister(@RequestParam("lecturer") String lecturerRequest, @RequestPart("avatar") MultipartFile avatar) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LecturerRequest lecturer = objectMapper.readValue(lecturerRequest, LecturerRequest.class);
            LecturerResponse lecturerResponse = authService.lecturerRegister(lecturer, avatar);
            return new ResponseEntity<>(lecturerResponse, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(Routing.IS_EXIST_EMAIL)
    public ResponseEntity<?> isExistEmail(@RequestParam String email) {
        if (userDetailServiceImpl.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email is already exist");
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping(Routing.IS_EXIST_USERNAME)
    public ResponseEntity<?> isExistUsername(@RequestParam String username) {
        if (userDetailServiceImpl.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username is already exist");
        } else {
            return ResponseEntity.ok().build();
        }
    }

}
