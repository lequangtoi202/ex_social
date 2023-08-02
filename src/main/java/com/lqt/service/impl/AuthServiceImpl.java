package com.lqt.service.impl;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.JwtResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.exception.SocialApiException;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.security.JwtTokenProvider;
import com.lqt.service.AuthService;
import com.lqt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userDetailsService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public JwtResponse login(UserDetails userDetails) {
        JwtResponse jwtResponse = tokenProvider.generateToken(userDetails);
        return jwtResponse;
    }

    @Override
    public AlumniResponse alumniRegister(AlumniRequest alumniRequest, MultipartFile avatar) {
        if (userDetailsService.existsByUsername(alumniRequest.getUsername())) {
            throw new SocialApiException(HttpStatus.BAD_REQUEST, "Username is already exist");
        }
        return userDetailsService.registerAlumni(alumniRequest, avatar);
    }

    @Override
    public UserDto userRegister(UserDto userDto, MultipartFile avatar) {
        if (userDetailsService.existsByUsername(userDto.getUsername())) {
            throw new SocialApiException(HttpStatus.BAD_REQUEST, "Username is already exist");
        }
        return userDetailsService.registerUserDto(userDto, avatar);
    }

    @Override
    public LecturerResponse lecturerRegister(LecturerRequest lecturerRequest, MultipartFile avatar) {
        if (userDetailsService.existsByUsername(lecturerRequest.getUsername())) {
            throw new SocialApiException(HttpStatus.BAD_REQUEST, "Username is already exist");
        }
        return userDetailsService.registerLecturer(lecturerRequest, avatar);
    }
}
