package com.lqt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerRequest {
    @NotNull
    @NotEmpty
    private String username;
    private String displayName;
    @NotBlank
    @NotNull
    private String email;
    private String fullName;
    private String phone;
}
