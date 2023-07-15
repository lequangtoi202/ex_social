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
@Table(name = "users")
public class User implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "username", nullable = true, length = 30)
    private String username;

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    private String password;

    @Basic
    @Column(name = "email", nullable = true, length = 45)
    private String email;

    @Basic
    @Column(name = "full_name", nullable = true, length = 100)
    private String fullName;

    @Basic
    @Column(name = "phone", nullable = true, length = 10)
    private String phone;

    @Basic
    @Column(name = "avatar", nullable = true, length = 255)
    private String avatar;

    @Basic
    @Column(name = "password_reset_token", nullable = true, length = 255)
    private String passwordResetToken;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Alumni> alumnis;

    @OneToMany(mappedBy = "userId")
    private List<Comment> comments;

    @OneToMany(mappedBy = "creatorId")
    private List<Group> groups;

    @OneToMany(mappedBy = "userId")
    private List<GroupsMembers> groupsMembers;

    @OneToMany(mappedBy = "userId")
    private List<Interaction> interactions;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<Lecturer> lecturers;

    @OneToMany(mappedBy = "userId")
    private List<Post> posts;

    @OneToMany(mappedBy = "userId")
    private List<Share> shares;

    @OneToMany(mappedBy = "userId")
    private List<Survey> surveys;

}
