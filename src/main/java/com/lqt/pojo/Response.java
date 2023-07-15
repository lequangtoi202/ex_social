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
@Table(name = "responses")
public class Response implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "content", nullable = true, length = 255)
    private String content;

    @Basic
    @Column(name = "surveys_id", nullable = false)
    private Long surveyId;

    @ManyToOne
    @JoinColumn(name = "options_id", referencedColumnName = "id", nullable = false)
    private Option option;

    @ManyToOne
    @JoinColumn(name = "questions_id", referencedColumnName = "id", nullable = false)
    private Question question;
}
