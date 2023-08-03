package com.lqt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumniRequest {
    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    private String password;
    private String confirmPassword;
    @NotBlank
    private String email;
    private String fullName;
    private String phone;
    @NotBlank
    @NotNull
    private String studentId;
}
