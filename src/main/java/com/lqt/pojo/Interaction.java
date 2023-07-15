package com.lqt.pojo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interactions")
public class Interaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "reaction_type", nullable = true, length = 10)
    private String reactionType;

    @Basic
    @Column(name = "timestamp", nullable = true)
    private Timestamp timestamp;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "posts_id", nullable = false)
    private Long postId;
}
