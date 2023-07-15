package com.lqt.pojo;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "questions")
public class Question implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    private String content;

    @Basic
    @Column(name = "surveys_id", nullable = false)
    private Long surveyId;

    @OneToMany(mappedBy = "questionId", cascade = CascadeType.ALL)
    private List<Option> options;

}
