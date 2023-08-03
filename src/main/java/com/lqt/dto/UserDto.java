package com.lqt.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @JsonIgnore
    @NotBlank
    private String confirmPassword;
    @NotBlank
    private String email;
    private String fullName;
    private String phone;
    private String avatarLink;
    private String resetPasswordToken;
    private String backgroundImage;
}
