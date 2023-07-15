package com.lqt.pojo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "alumni")
public class Alumni implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "student_id", nullable = true, length = 10)
    private String studentId;

    @Basic
    @Column(name = "is_confirmed", nullable = true)
    private Boolean isConfirmed;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;
}
