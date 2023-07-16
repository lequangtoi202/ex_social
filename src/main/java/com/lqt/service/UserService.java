package com.lqt.service;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    AlumniResponse registerAlumni(AlumniRequest alumniRequest, MultipartFile avatar);
    LecturerResponse registerLecturer(LecturerRequest lecturerRequest, MultipartFile avatar);
    UserDto registerUserDto(UserDto UserDto, MultipartFile avatar);
    UserDto findByEmail(String email);
    UserDto findUserById(Long id);
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User getMyAccount(String username);
    UserDto findByResetPasswordToken(String token);
    void updateResetPassword(String token, String email);
    Boolean assignRoleToUser(Role role, Long id);
    LecturerResponse resetWaitingChangePassword(Long id);
    UserDto changePassword(Long id, String password, String confirmPassword);
    LecturerResponse updateProfileLecturer(LecturerRequest lecturerRequest, Long id);
    AlumniResponse updateProfileAlumni(AlumniRequest alumniRequest, Long id);
    LecturerResponse updatePasswordLecturer(Long id, String password, String confirmPassword);
    List<Role> getAllRoleOfUser(Long userId);
}
