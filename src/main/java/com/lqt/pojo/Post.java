package com.lqt.pojo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    private String content;

    @Basic
    @Column(name = "timestamp", nullable = true)
    private Timestamp timestamp;

    @Basic
    @Column(name = "is_survey", nullable = true)
    private Boolean isSurvey;

    @Basic
    @Column(name = "is_locked", nullable = true)
    private Boolean isLocked;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "postId")
    private List<Interaction> interactions;
}
