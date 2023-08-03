package com.lqt.controllers;

import com.lqt.dto.*;
import com.lqt.pojo.Alumni;
import com.lqt.pojo.Post;
import com.lqt.pojo.User;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.service.AuthService;
import com.lqt.service.UserService;
import com.lqt.util.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userDetailServiceImpl;

    @PostMapping(Routing.LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) throws Exception {
        authenticate(loginDto.getUsername(), loginDto.getPassword());

        final UserDetails userDetails = userDetailServiceImpl
                .loadUserByUsername(loginDto.getUsername());
        User user = userDetailServiceImpl.findByUsername(userDetails.getUsername());
        Alumni alumni = userDetailServiceImpl.findAlumniByUserId(user.getId());
        if (alumni != null){
            Boolean isConfirmed = userDetailServiceImpl.checkAlumniIsConfirmed(alumni);
            if (isConfirmed){
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
            else{
                return new ResponseEntity<>("Your account is not confirmed by admin. Please waiting!..", HttpStatus.BAD_REQUEST);
            }
        }else{
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

    @PostMapping(Routing.ADMIN_REGISTER)
    public void adminRegister(@RequestBody @Valid UserDto userDto, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        authService.userRegister(userDto, avatar);
    }

    @PostMapping(Routing.ALUMNI_REGISTER)
    public ResponseEntity<AlumniResponse> alumniRegister(@RequestBody @Valid AlumniRequest alumniRequest, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        AlumniResponse alumniResponse = authService.alumniRegister(alumniRequest, avatar);
        return new ResponseEntity<>(alumniResponse, HttpStatus.CREATED);
    }

    @PostMapping(Routing.LECTURER_REGISTER)
    public ResponseEntity<LecturerResponse> lecturerRegister(@RequestBody @Valid LecturerRequest lecturerRequest, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        LecturerResponse lecturerResponse = authService.lecturerRegister(lecturerRequest, avatar);
        return new ResponseEntity<>(lecturerResponse, HttpStatus.CREATED);
    }

    @PostMapping(Routing.IS_EXIST_EMAIL)
    public ResponseEntity<?> isExistEmail(@RequestParam String email){
        if (userDetailServiceImpl.existsByEmail(email)){
            return ResponseEntity.badRequest().body("Email is already exist");
        }else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping(Routing.IS_EXIST_USERNAME)
    public ResponseEntity<?> isExistUsername(@RequestParam String username){
        if (userDetailServiceImpl.existsByUsername(username)){
            return ResponseEntity.badRequest().body("Username is already exist");
        }else {
            return ResponseEntity.ok().build();
        }
    }

}
