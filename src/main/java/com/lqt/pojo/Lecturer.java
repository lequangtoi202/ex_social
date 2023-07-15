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
@Table(name = "lecturer")
public class Lecturer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "is_locked", nullable = true)
    private Boolean isLocked;

    @Basic
    @Column(name = "created_on", nullable = true)
    private Timestamp createdOn;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;
}
