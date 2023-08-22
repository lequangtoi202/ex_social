package com.lqt.service;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.JwtResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    JwtResponse login(UserDetails userDetails);

    AlumniResponse alumniRegister(AlumniRequest alumniRequest, MultipartFile avatar);

    UserDto userRegister(UserDto userDto, MultipartFile avatar);

    LecturerResponse lecturerRegister(LecturerRequest lecturerRequest, MultipartFile avatar);
}
