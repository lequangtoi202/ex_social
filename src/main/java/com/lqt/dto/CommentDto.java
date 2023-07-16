package com.lqt.dto;

import com.lqt.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String content;
    private Timestamp timestamp;
    private Long postId;
    private Long userId;
    private Long belongsComment;
}
