package com.lqt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumniRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String fullName;
    private String phone;
    private String studentId;
}
