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
@Table(name = "comments")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "content", nullable = true, length = -1)
    private String content;

    @Basic
    @Column(name = "timestamp", nullable = true)
    private Timestamp timestamp;

    @Basic
    @Column(name = "posts_id", nullable = false)
    private Long postId;

    @Basic
    @Column(name = "users_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "belongs_comments_id", referencedColumnName = "id", nullable = true)
    private Comment belongsComment;

    @OneToMany(mappedBy = "belongsComment")
    private List<Comment> comments;
}
