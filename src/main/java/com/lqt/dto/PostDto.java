package com.lqt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    @NotBlank
    private String content;
    private Timestamp timestamp;
    private Boolean isSurvey;
    private Boolean isLocked;
    private Long userId;
}
