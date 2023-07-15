package com.lqt.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerRequest {
    private String username;
    private String email;
    private String fullName;
    private String phone;
}
