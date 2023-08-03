package com.lqt.service;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.pojo.Alumni;
import com.lqt.pojo.User;
import com.lqt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void RegisterAlumniTest() {
        User user = new User();
        user.setId(1L);
        user.setAvatar(null);
        user.setEmail("toiquangle@gmail.com");
        user.setFullName("Lê Quang Tới");
        user.setPassword(passwordEncoder.encode("123"));
        user.setPhone("0868832530");
        user.setUsername("quangtoi");
        user.setBackgroundImage(null);
        Alumni alumni = Alumni.builder()
                .studentId("2051052140")
                .isConfirmed(false).build();
        AlumniResponse alumniResponse = AlumniResponse.builder()
                .studentId("2051052140")
                .isConfirmed(false)
                .userId(1L)
                .id(1L)
                .avatarLink(null)
                .fullName("Lê Quang Tới")
                .phone("0868832530")
                .email("toiquangle@gmail.com")
                .username("quangtoi")
                .password(passwordEncoder.encode("123"))
                .bgImage(null)
                .build();
        userRepository.registerAlumni(alumni, user);
        Mockito.verify(userRepository, Mockito.times(1))
                .registerAlumni(any(), any());
        Mockito.when(userRepository.registerAlumni(alumni, user)).thenReturn(alumniResponse);

        Assertions.assertEquals("toiquangle@gmail.com", alumniResponse.getEmail());
        Assertions.assertEquals(1L, alumniResponse.getUserId());
        Assertions.assertEquals("quangtoi", alumniResponse.getUsername());
        Assertions.assertEquals(1L, alumniResponse.getId());
        Assertions.assertEquals(null, alumniResponse.getBgImage());
        Assertions.assertEquals(null, alumniResponse.getAvatarLink());
        Assertions.assertFalse(alumniResponse.getIsConfirmed());
        Assertions.assertEquals("toiquangle@gmail.com", alumniResponse.getEmail());
        Assertions.assertEquals("Lê Quang Tới", alumniResponse.getFullName());
        Assertions.assertEquals("0868832530", alumniResponse.getPhone());
        Assertions.assertEquals("2051052140", alumniResponse.getStudentId());
        Assertions.assertEquals(passwordEncoder.encode("123"), alumniResponse.getPassword());
    }

    @Test
    public void resetForLecturerUpdateNewPasswordTest() {
        User user = User.builder()
                .id(2L)
                .fullName("Lê Quang Tới")
                .avatar(null)
                .password(passwordEncoder.encode("123"))
                .backgroundImage(null)
                .email("toiquangle@gmail.com")
                .passwordResetToken(null)
                .phone("0868832530")
                .username("quangtoi")
                .build();
        LecturerResponse lecturerResponse = LecturerResponse
                .builder()
                .avatarLink(user.getAvatar())
                .bgImage(user.getBackgroundImage())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .isLocked(false)
                .username(user.getUsername())
                .password(user.getPassword())
                .createdOn(Timestamp.valueOf(LocalDateTime.now()))
                .userId(user.getId())
                .id(1L)
                .build();
        userRepository.resetWaitingChangePassword(user);
        Mockito.when(userRepository.resetWaitingChangePassword(user)).thenReturn(lecturerResponse);
        Mockito.verify(userRepository, Mockito.times(1))
                .resetWaitingChangePassword(any());

        Assertions.assertEquals("toiquangle@gmail.com", lecturerResponse.getEmail());
        Assertions.assertEquals(2L, lecturerResponse.getUserId());
        Assertions.assertEquals("quangtoi", lecturerResponse.getUsername());
        Assertions.assertEquals(1L, lecturerResponse.getId());
        Assertions.assertEquals(null, lecturerResponse.getBgImage());
        Assertions.assertEquals(null, lecturerResponse.getAvatarLink());
        Assertions.assertFalse(lecturerResponse.getIsLocked());
        Mockito.when(userRepository.resetWaitingChangePassword(user))
                .thenReturn(ArgumentMatchers.argThat(response ->
                        response.getCreatedOn().equals(lecturerResponse.getCreatedOn())
                ));
        Assertions.assertEquals("toiquangle@gmail.com", lecturerResponse.getEmail());
        Assertions.assertEquals("Lê Quang Tới", lecturerResponse.getFullName());
        Assertions.assertEquals("0868832530", lecturerResponse.getPhone());
        Assertions.assertEquals(passwordEncoder.encode("123"), lecturerResponse.getPassword());
    }

    @Test
    public void findByResetPasswordTokenTest() {
        String token = RandomString.make(45);
        UserDto u = UserDto.builder()
                .resetPasswordToken(token)
                .id(2L)
                .fullName("Lê Quang Tới")
                .avatarLink(null)
                .password(passwordEncoder.encode("123"))
                .backgroundImage(null)
                .email("toiquangle@gmail.com")
                .phone("0868832530")
                .username("quangtoi")
                .build();
        userRepository.findByResetPasswordToken(token);
        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(userRepository, Mockito.times(1))
                .findByResetPasswordToken(tokenCaptor.capture());
        Mockito.when(userRepository.findByResetPasswordToken(any())).thenReturn(u);
        Assertions.assertEquals("toiquangle@gmail.com", u.getEmail());
        Assertions.assertEquals("quangtoi", u.getUsername());
        Assertions.assertEquals(2L, u.getId());
        Assertions.assertEquals(null, u.getBackgroundImage());
        Assertions.assertEquals(null, u.getAvatarLink());
        Assertions.assertEquals("toiquangle@gmail.com", u.getEmail());
        Assertions.assertEquals("Lê Quang Tới", u.getFullName());
        Assertions.assertEquals("0868832530", u.getPhone());
        Assertions.assertEquals(passwordEncoder.encode("123"), u.getPassword());
        Assertions.assertEquals(token, tokenCaptor.getValue());
    }

    @Test
    public void changePasswordOfUserTest() {
        String token = RandomString.make(45);
        User u = User.builder()
                .passwordResetToken(token)
                .id(2L)
                .fullName("Lê Quang Tới")
                .avatar(null)
                .password(passwordEncoder.encode("1234"))
                .backgroundImage(null)
                .email("toiquangle@gmail.com")
                .phone("0868832530")
                .username("quangtoi")
                .build();
        UserDto userDto = UserDto.builder()
                .resetPasswordToken(token)
                .id(2L)
                .fullName("Lê Quang Tới")
                .avatarLink(null)
                .password(passwordEncoder.encode("1234"))
                .backgroundImage(null)
                .email("toiquangle@gmail.com")
                .phone("0868832530")
                .username("quangtoi")
                .build();
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userRepository.changePassword(u);
        Mockito.verify(userRepository, Mockito.times(1))
                .changePassword(userArgumentCaptor.capture());
        Mockito.when(userRepository.changePassword(any())).thenReturn(userDto);
        Assertions.assertEquals("toiquangle@gmail.com", userDto.getEmail());
        Assertions.assertEquals("quangtoi", userDto.getUsername());
        Assertions.assertEquals(2L, userDto.getId());
        Assertions.assertEquals(null, userDto.getBackgroundImage());
        Assertions.assertEquals(null, userDto.getAvatarLink());
        Assertions.assertEquals("toiquangle@gmail.com", userDto.getEmail());
        Assertions.assertEquals("Lê Quang Tới", userDto.getFullName());
        Assertions.assertEquals("0868832530", userDto.getPhone());
        Assertions.assertEquals(passwordEncoder.encode("1234"), userDto.getPassword());
        Assertions.assertEquals(u, userArgumentCaptor.getValue());
    }
}
