package com.lqt.service.impl;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Alumni;
import com.lqt.pojo.Lecturer;
import com.lqt.pojo.Role;
import com.lqt.pojo.User;
import com.lqt.repository.UserRepository;
import com.lqt.request.AlumniRequest;
import com.lqt.request.LecturerRequest;
import com.lqt.request.MailRequest;
import com.lqt.service.ImageService;
import com.lqt.service.MailService;
import com.lqt.service.UserService;
import com.lqt.util.HTMLConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lqt.util.Constants.YYYY_MM_DD;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AlumniResponse registerAlumni(AlumniRequest alumniRequest, MultipartFile avatar) {
        String password = null;
        if (alumniRequest.getPassword().equals(alumniRequest.getConfirmPassword())) {
            password = passwordEncoder.encode(alumniRequest.getPassword());
        }
        User user = new User();
        if (avatar == null) {
            user.setAvatar(null);
        } else {
            String urlImage = imageService.uploadImage(avatar);
            user.setAvatar(urlImage);
        }
        user.setEmail(alumniRequest.getEmail());
        user.setFullName(alumniRequest.getFullName());
        user.setDisplayName(alumniRequest.getDisplayName());
        user.setPassword(password);
        user.setPhone(alumniRequest.getPhone());
        user.setUsername(alumniRequest.getUsername());
        user.setBackgroundImage(null);

        Alumni alumni = Alumni.builder()
                .isConfirmed(false)
                .studentId(alumniRequest.getStudentId())
                .build();

        return userRepository.registerAlumni(alumni, user);
    }

    @Override
    public LecturerResponse registerLecturer(LecturerRequest lecturerRequest, MultipartFile avatar) {
        User user = new User();
        if (avatar == null) {
            user.setAvatar(null);
        } else {
            String urlImage = imageService.uploadImage(avatar);
            user.setAvatar(urlImage);
        }
        user.setEmail(lecturerRequest.getEmail());
        user.setFullName(lecturerRequest.getFullName());
        user.setDisplayName(lecturerRequest.getDisplayName());
        user.setPassword(passwordEncoder.encode("ou@123"));
        user.setPhone(lecturerRequest.getPhone());
        user.setUsername(lecturerRequest.getUsername());
        user.setBackgroundImage(null);

        Lecturer lecturer = Lecturer.builder()
                .isLocked(false)
                .createdOn(Timestamp.valueOf(LocalDateTime.now()))
                .build();
        return userRepository.registerLecturer(lecturer, user);
    }

    @Override
    public UserDto registerUserDto(UserDto userDto, MultipartFile avatar) {
        String password = null;
        if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            password = passwordEncoder.encode(userDto.getPassword());
        }
        User user = new User();
        if (avatar == null) {
            user.setAvatar(null);
        } else {
            String urlImage = imageService.uploadImage(avatar);
            user.setAvatar(urlImage);
        }
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setDisplayName(userDto.getDisplayName());
        user.setPassword(password);
        user.setPhone(userDto.getPhone());
        user.setUsername(userDto.getUsername());
        user.setBackgroundImage(null);

        return mapper.map(userRepository.registerUser(user), UserDto.class);
    }

    @Override
    public Boolean deleteUser(Long userId, Long adminId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        List<Role> roles = this.getAllRoleOfUser(adminId);
        Boolean hasAdminRole = roles.stream().anyMatch(r -> r.getName().equals("SYS_ADMIN"));
        if (hasAdminRole) {
            return userRepository.deleteUser(user);
        } else {
            return null;
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        return mapper.map(userRepository.findByEmail(email), UserDto.class);
    }

    @Override
    public List<UserDto> findByFullName(String name) {
        List<User> users = userRepository.findByFullName(name);
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(u -> {
            UserDto userDto = UserDto.builder()
                    .avatarLink(u.getAvatar())
                    .username(u.getUsername())
                    .displayName(u.getDisplayName())
                    .email(u.getEmail())
                    .fullName(u.getFullName())
                    .password(u.getPassword())
                    .phone(u.getPhone())
                    .id(u.getId())
                    .backgroundImage(u.getBackgroundImage())
                    .resetPasswordToken(u.getPasswordResetToken())
                    .build();
            userDtos.add(userDto);
        });
        return userDtos;
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id);
        UserDto userDto = UserDto.builder()
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .backgroundImage(user.getBackgroundImage())
                .id(user.getId())
                .phone(user.getPhone())
                .password(user.getPassword())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .resetPasswordToken(user.getPasswordResetToken())
                .avatarLink(user.getAvatar())
                .build();
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(u -> {
            UserDto userDto = UserDto.builder()
                    .avatarLink(u.getAvatar())
                    .username(u.getUsername())
                    .displayName(u.getDisplayName())
                    .email(u.getEmail())
                    .fullName(u.getFullName())
                    .password(u.getPassword())
                    .phone(u.getPhone())
                    .id(u.getId())
                    .backgroundImage(u.getBackgroundImage())
                    .resetPasswordToken(u.getPasswordResetToken())
                    .build();
            userDtos.add(userDto);
        });
        return userDtos;
    }

    @Override
    public Alumni findAlumniByUserId(Long userId) {
        return userRepository.findAlumniByUserId(userId);
    }

    @Override
    public Lecturer findLecturerByUserId(Long userId) {
        return userRepository.findLecturerByUserId(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getMyAccount(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDto findByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    @Override
    public void updateResetPassword(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPasswordResetToken(token);
            userRepository.updateUser(user);
        } else {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

    @Override
    public void sendMailAccountToLecturer(String email, String username) {
        String html = "Username: " + username + " hoặc " + email + "\n";
        html += "Password: ou@123. Vui lòng thay đổi mật khẩu trong vòng 24h.";
        String body = HTMLConverter.convertToHTML(html);
        MailRequest mailRequest = MailRequest.builder()
                .from("2051052140toi@ou.edu.vn")
                .subject("Cấp tài khoản giảng viên")
                .body(body)
                .recipients(email)
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern(YYYY_MM_DD)))
                .build();
        mailService.sendMailAccountToLecturer(mailRequest);
    }

    @Override
    public Boolean assignRoleToUser(Role role, Long id) {
        User u = userRepository.findById(id);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        List<Role> roles = this.getAllRoleOfUser(u.getId());
        boolean hasSysAdminRole = false;

        for (Role r : roles) {
            if (r.getName().equals("SYS_ADMIN")) {
                hasSysAdminRole = true;
                break;
            }
        }
        if (hasSysAdminRole) {
            throw new RuntimeException("User này đã là admin");
        }
        return userRepository.assignRoleToUser(role, u);
    }

    @Override
    public LecturerResponse resetWaitingChangePassword(Long id) {
        User u = userRepository.findById(id);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        return userRepository.resetWaitingChangePassword(u);
    }

    @Override
    public UserDto changePassword(Long id, String password, String confirmPassword) {
        if (!password.isEmpty() && confirmPassword.equals(password)) {
            User user = userRepository.findById(id);
            if (user == null) {
                throw new ResourceNotFoundException("User", "id", id);
            }
            user.setPassword(passwordEncoder.encode(password));
            UserDto userRes = userRepository.changePassword(user);
            return userRes;
        }
        return null;
    }

    @Override
    public LecturerResponse updateProfileLecturer(LecturerRequest lecturerRequest, Long id) {
        User u = userRepository.findById(id);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        Lecturer lecturer = userRepository.findLecturerByUserId(u.getId());
        if (lecturer == null) {
            throw new ResourceNotFoundException("Lecturer", "user_id", u.getId());
        }
        u.setEmail(lecturerRequest.getEmail());
        u.setPhone(lecturerRequest.getPhone());
        u.setDisplayName(lecturerRequest.getDisplayName());
        u.setFullName(lecturerRequest.getFullName());
        u.setPhone(lecturerRequest.getPhone());
        u.setBackgroundImage(null);

        return userRepository.updateProfileLecturer(u, lecturer);
    }

    @Override
    public AlumniResponse updateProfileAlumni(AlumniRequest alumniRequest, Long id) {
        User u = userRepository.findById(id);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        Alumni alumni = userRepository.findAlumniByUserId(u.getId());
        if (alumni == null) {
            throw new ResourceNotFoundException("Alumni", "user_id", u.getId());
        }
        u.setEmail(alumniRequest.getEmail());
        u.setDisplayName(alumniRequest.getDisplayName());
        u.setPhone(alumniRequest.getPhone());
        u.setFullName(alumniRequest.getFullName());
        u.setBackgroundImage(null);

        alumni.setStudentId(alumniRequest.getStudentId());

        return userRepository.updateProfileAlumni(u, alumni);
    }

    @Override
    public List<AlumniResponse> getAllAlumniIsNotConfirmed() {
        return userRepository.getAllAlumniIsNotConfirmed();
    }

    @Override
    public LecturerResponse updatePasswordLecturer(Long id, String password, String confirmPassword) {
        User u = userRepository.findById(id);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        if (!password.isEmpty() && confirmPassword.equals(password)) {

            u.setPassword(passwordEncoder.encode(password));
            return userRepository.updatePasswordLecturer(u);
        }
        return null;
    }

    @Override
    public List<Role> getAllRoleOfUser(Long userId) {
        User u = userRepository.findById(userId);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return userRepository.getAllRoleOfUser(u);
    }

    @Override
    public Boolean confirmAlumni(Long userId) {
        User u = userRepository.findById(userId);
        if (u == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        Alumni alumni = userRepository.findAlumniByUserId(u.getId());
        if (alumni == null) {
            throw new ResourceNotFoundException("Alumni", "userId", u.getId());
        }
        return userRepository.confirmAlumni(alumni);
    }

    @Override
    public Boolean checkAlumniIsConfirmed(Alumni alumni) {
        return userRepository.checkAlumniIsConfirmed(alumni);
    }

    @Override
    public Boolean checkLecturerIsLocked(Lecturer lecturer) {
        return userRepository.checkLecturerIsLocked(lecturer);
    }

    @Override
    public void uploadBgImage(MultipartFile bgImage, User user) {
        if (bgImage == null) {
            user.setBackgroundImage(null);
        } else {
            String urlImage = imageService.uploadImage(bgImage);
            user.setBackgroundImage(urlImage);
        }
        userRepository.updateUser(user);
    }

    @Override
    public List<AlumniResponse> getAllAlumni() {
        return userRepository.getAllAlumni();
    }

    @Override
    public List<LecturerResponse> getAllLecturers() {
        return userRepository.getAllLecturers();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        List<Role> roles = userRepository.getAllRoleOfUser(user);
        Set<GrantedAuthority> authorities = roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
