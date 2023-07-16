package com.lqt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniDto {
    private Long id;
    private String studentId;
    private Boolean isConfirmed;
    private Long userId;
}
