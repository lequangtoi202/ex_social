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
@Table(name = "surveys")
public class Survey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "title", nullable = true, length = 100)
    private String title;

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;

    @Basic
    @Column(name = "type", nullable = true, length = 100)
    private String type;

    @Basic
    @Column(name = "created_on")
    private Timestamp createdOn;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "posts_id", referencedColumnName = "id")
    private Post post;

    @OneToMany(mappedBy = "surveyId", cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "surveyId", cascade = CascadeType.ALL)
    private List<Response> responses;
}
