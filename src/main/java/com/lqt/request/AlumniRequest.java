package com.lqt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlumniRequest {
    private String username;
    private String displayName;
    private String password;
    private String confirmPassword;
    private String email;
    private String fullName;
    private String phone;
    private String studentId;
}
