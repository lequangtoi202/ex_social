package com.lqt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    @JsonIgnore
    private String confirmPassword;
    private String email;
    private String fullName;
    private String phone;
    private String avatarLink;
    private String resetPasswordToken;
    private String backgroundImage;
}
