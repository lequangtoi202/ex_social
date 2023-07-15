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
@Table(name = "groups")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "group_name", nullable = true, length = 100)
    private String groupName;

    @Basic
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

}
