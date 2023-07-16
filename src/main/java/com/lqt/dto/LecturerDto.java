package com.lqt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturerDto {
    private Long id;
    private Boolean isLocked;
    private Timestamp createdOn;
    private Long userId;
}
