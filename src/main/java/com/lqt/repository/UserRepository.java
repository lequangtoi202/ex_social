package com.lqt.repository;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.pojo.Alumni;
import com.lqt.pojo.Lecturer;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserRepository {
    AlumniResponse registerAlumni(Alumni alumni, User u);

    LecturerResponse registerLecturer(Lecturer lecturer, User u);

    User registerUser(User u);

    User findByEmail(String email);

    List<User> findByFullName(String name);

    User updateUser(User user);

    Alumni findAlumniByUserId(Long userId);

    Lecturer findLecturerByUserId(Long userId);

    User findById(Long id);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    UserDto findByResetPasswordToken(String token);

    Boolean assignRoleToUser(Role role, User u);

    LecturerResponse resetWaitingChangePassword(User u);

    UserDto changePassword(User u);

    LecturerResponse updateProfileLecturer(User u, Lecturer l);

    AlumniResponse updateProfileAlumni(User u, Alumni alumni);

    List<AlumniResponse> getAllAlumniIsNotConfirmed();

    List<AlumniResponse> getAllAlumni();

    List<LecturerResponse> getAllLecturers();

    List<User> getAllUsers();

    LecturerResponse updatePasswordLecturer(User u);

    Boolean lockLecturerWithoutChangeDefaultPassword();

    Boolean deleteUser(User user);

    List<Role> getAllRoleOfUser(User u);

    Boolean confirmAlumni(Alumni alumni);

    Boolean checkAlumniIsConfirmed(Alumni alumni);

}
