package com.lqt.controllers;

import com.lqt.dto.*;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userDetailServiceImpl;

    @PostMapping(Routing.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws Exception {

        authenticate(loginDto.getUsername(), loginDto.getPassword());

        final UserDetails userDetails = userDetailServiceImpl
                .loadUserByUsername(loginDto.getUsername());
        JwtResponse jwtResponse = authService.login(userDetails);
        if (jwtResponse != null) {
            return ResponseEntity.ok().body(jwtResponse);
        } else {
            return ResponseEntity.badRequest().body("Username or password is invalid!");
        }
    }

    private void authenticate(String username, String password) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //hàm này nên chuyển qua controller của admin -> chỉnh sửa lại
    @PostMapping(Routing.ADMIN_REGISTER)
    public void adminRegister(@RequestBody UserDto userDto, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        authService.userRegister(userDto, avatar);
    }

    @PostMapping(Routing.ALUMNI_REGISTER)
    public ResponseEntity<AlumniResponse> alumniRegister(@RequestBody AlumniRequest alumniRequest, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        AlumniResponse alumniResponse = authService.alumniRegister(alumniRequest, avatar);
        return new ResponseEntity<>(alumniResponse, HttpStatus.CREATED);
    }

    @PostMapping(Routing.LECTURER_REGISTER)
    public ResponseEntity<LecturerResponse> lecturerRegister(@RequestBody LecturerRequest lecturerRequest, @RequestPart("avatar")MultipartFile avatar) throws Exception {
        LecturerResponse lecturerResponse = authService.lecturerRegister(lecturerRequest, avatar);
        return new ResponseEntity<>(lecturerResponse, HttpStatus.CREATED);
    }


}
