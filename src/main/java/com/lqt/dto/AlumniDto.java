package com.lqt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlumniDto {
    private Long id;
    @NotBlank
    @NotNull
    @Size(min = 10, max = 10)
    private String studentId;
    private Boolean isConfirmed;
    private Long userId;
}
