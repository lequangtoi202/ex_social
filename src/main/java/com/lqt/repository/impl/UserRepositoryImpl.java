package com.lqt.repository.impl;

import com.lqt.dto.AlumniResponse;
import com.lqt.dto.LecturerResponse;
import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.*;
import com.lqt.repository.RoleRepository;
import com.lqt.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public AlumniResponse registerAlumni(Alumni alumni, User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Role role = roleRepository.getRoleByName("ALUMNI");
        if (role == null) {
            throw new ResourceNotFoundException("Role", "name", "ALUMNI");
        }
        s.save(u);
        alumni.setUserId(u.getId());
        s.save(alumni);

        UsersRoles usersRoles = UsersRoles.builder()
                .role(role)
                .user(u).build();
        s.save(usersRoles);

        AlumniResponse alumniResponse = AlumniResponse
                .builder()
                .avatarLink(u.getAvatar())
                .email(u.getEmail())
                .password(u.getPassword())
                .fullName(u.getFullName())
                .displayName(u.getDisplayName())
                .username(u.getUsername())
                .studentId(alumni.getStudentId())
                .phone(u.getPhone())
                .isConfirmed(alumni.getIsConfirmed())
                .userId(u.getId())
                .id(alumni.getId())
                .build();
        return alumniResponse;
    }

    @Override
    public LecturerResponse registerLecturer(Lecturer lecturer, User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Role role = roleRepository.getRoleByName("LECTURER");
        if (role == null) {
            throw new ResourceNotFoundException("Role", "name", "LECTURER");
        }
        s.save(u);
        lecturer.setUserId(u.getId());
        s.save(lecturer);

        UsersRoles usersRoles = UsersRoles.builder()
                .role(role)
                .user(u).build();
        s.save(usersRoles);

        LecturerResponse lecturerResponse = LecturerResponse
                .builder()
                .avatarLink(u.getAvatar())
                .bgImage(u.getBackgroundImage())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .displayName(u.getDisplayName())
                .phone(u.getPhone())
                .isLocked(lecturer.getIsLocked())
                .username(u.getUsername())
                .password(u.getPassword())
                .createdOn(lecturer.getCreatedOn())
                .userId(u.getId())
                .id(lecturer.getId())
                .build();
        return lecturerResponse;
    }

    @Override
    public User registerUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Role role = roleRepository.getRoleByName("SYS_ADMIN");
        if (role == null) {
            throw new ResourceNotFoundException("Role", "name", "SYS_ADMIN");
        }
        s.save(u);
        UsersRoles usersRoles = UsersRoles.builder()
                .role(role)
                .user(u).build();
        s.save(usersRoles);
        return u;
    }

    @Override
    public User findByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.email =:email");
        q.setParameter("email", email);
        q.setMaxResults(1);
        User user = null;
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException e) {

        } finally {
            return user;
        }
    }

    @Override
    public List<User> findByFullName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.fullName like CONCAT('%', :name, '%')");
        q.setParameter("name", name);
        List<User> users = null;
        try {
            users = q.getResultList();
        } catch (NoResultException e) {

        } finally {
            return users;
        }
    }

    @Override
    public User updateUser(User user) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(user);
        return user;
    }

    @Override
    public Alumni findAlumniByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT a FROM Alumni a WHERE a.userId = :userId");
        q.setParameter("userId", userId);
        try {
            Alumni alumni = (Alumni) q.getSingleResult();
            return alumni;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Lecturer findLecturerByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT l FROM Lecturer l WHERE l.userId = :userId");
        q.setParameter("userId", userId);
        try {
            Lecturer lecturer = (Lecturer) q.getSingleResult();
            return lecturer;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        User u = s.get(User.class, id);
        if (u != null) {
            return u;
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.username =:username");
        q.setParameter("username", username);
        q.setMaxResults(1);
        User user = null;
        try {
            user = (User) q.getSingleResult();
        } catch (NoResultException e) {
            throw new NoSuchFieldException();
        } finally {
            return user;
        }
    }

    @Override
    public Boolean existsByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.username =:username");
        q.setParameter("username", username);
        q.setMaxResults(1);
        User user = null;
        try {
            user = (User) q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.email =:email");
        q.setParameter("email", email);
        User user = null;
        try {
            user = (User) q.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public UserDto findByResetPasswordToken(String token) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("select u from User u where u.passwordResetToken =:token");
        q.setParameter("token", token);
        User user = null;
        try {
            user = (User) q.getSingleResult();
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .displayName(user.getDisplayName())
                    .password(user.getPassword())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .backgroundImage(user.getBackgroundImage())
                    .avatarLink(user.getAvatar())
                    .resetPasswordToken(user.getPasswordResetToken())
                    .build();
            return userDto;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean assignRoleToUser(Role role, User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Role roleSaved = roleRepository.getRoleByName(role.getName());
        UsersRoles usersRoles = UsersRoles.builder()
                .role(roleSaved)
                .user(u)
                .build();
        try {
            s.save(usersRoles);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public LecturerResponse resetWaitingChangePassword(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT l FROM Lecturer l WHERE l.userId = :userId");
        q.setParameter("userId", u.getId());
        Lecturer lecturer = (Lecturer) q.getSingleResult();
        lecturer.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        lecturer.setIsLocked(false);
        s.save(lecturer);

        LecturerResponse lecturerResponse = LecturerResponse
                .builder()
                .avatarLink(u.getAvatar())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .bgImage(u.getBackgroundImage())
                .isLocked(lecturer.getIsLocked())
                .displayName(u.getDisplayName())
                .username(u.getUsername())
                .password(u.getPassword())
                .createdOn(lecturer.getCreatedOn())
                .userId(u.getId())
                .id(lecturer.getId())
                .build();
        return lecturerResponse;
    }

    @Override
    public UserDto changePassword(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(u);
        UserDto userDto = UserDto.builder()
                .id(u.getId())
                .avatarLink(u.getAvatar())
                .password(u.getPassword())
                .fullName(u.getFullName())
                .username(u.getUsername())
                .email(u.getEmail())
                .displayName(u.getDisplayName())
                .phone(u.getPhone())
                .resetPasswordToken(u.getPasswordResetToken())
                .build();
        return userDto;
    }

    @Override
    public LecturerResponse updateProfileLecturer(User u, Lecturer l) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(u);
        s.update(l);
        LecturerResponse lecturerResponse = LecturerResponse
                .builder()
                .avatarLink(u.getAvatar())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .isLocked(l.getIsLocked())
                .displayName(u.getDisplayName())
                .username(u.getUsername())
                .password(u.getPassword())
                .createdOn(l.getCreatedOn())
                .userId(u.getId())
                .id(l.getId())
                .build();
        return lecturerResponse;
    }

    @Override
    public AlumniResponse updateProfileAlumni(User u, Alumni alumni) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(u);
        s.update(alumni);
        AlumniResponse alumniResponse = AlumniResponse
                .builder()
                .avatarLink(u.getAvatar())
                .email(u.getEmail())
                .password(u.getPassword())
                .fullName(u.getFullName())
                .displayName(u.getDisplayName())
                .username(u.getUsername())
                .studentId(alumni.getStudentId())
                .phone(u.getPhone())
                .isConfirmed(alumni.getIsConfirmed())
                .userId(u.getId())
                .id(alumni.getId())
                .build();
        return alumniResponse;
    }

    @Override
    public List<AlumniResponse> getAllAlumniIsNotConfirmed() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT a FROM Alumni a WHERE a.isConfirmed=false");
        List<Alumni> alumniList = q.getResultList();
        List<AlumniResponse> alumniResponses = new ArrayList<>();
        alumniList.forEach(a -> {
            User user = this.findById(a.getUserId());
            AlumniResponse alumniResponse = AlumniResponse.builder()
                    .avatarLink(user.getAvatar())
                    .isConfirmed(a.getIsConfirmed())
                    .fullName(user.getFullName())
                    .id(a.getId())
                    .displayName(user.getDisplayName())
                    .userId(a.getUserId())
                    .phone(user.getPhone())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .studentId(a.getStudentId())
                    .build();
            alumniResponses.add(alumniResponse);
        });
        return alumniResponses;
    }

    @Override
    public List<AlumniResponse> getAllAlumni() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT a FROM Alumni a");
        List<Alumni> alumniList = q.getResultList();
        List<AlumniResponse> alumniResponses = new ArrayList<>();
        alumniList.forEach(a -> {
            User user = this.findById(a.getUserId());
            AlumniResponse alumniResponse = AlumniResponse.builder()
                    .avatarLink(user.getAvatar())
                    .isConfirmed(a.getIsConfirmed())
                    .fullName(user.getFullName())
                    .id(a.getId())
                    .displayName(user.getDisplayName())
                    .userId(a.getUserId())
                    .phone(user.getPhone())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .studentId(a.getStudentId())
                    .build();
            alumniResponses.add(alumniResponse);
        });
        return alumniResponses;
    }

    @Override
    public List<LecturerResponse> getAllLecturers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT l FROM Lecturer l");
        List<Lecturer> lecturerList = q.getResultList();
        List<LecturerResponse> lecturerResponses = new ArrayList<>();
        lecturerList.forEach(a -> {
            User user = this.findById(a.getUserId());
            LecturerResponse lecturerResponse = LecturerResponse.builder()
                    .avatarLink(user.getAvatar())
                    .fullName(user.getFullName())
                    .id(a.getId())
                    .userId(a.getUserId())
                    .isLocked(a.getIsLocked())
                    .displayName(user.getFullName())
                    .phone(user.getPhone())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .build();
            lecturerResponses.add(lecturerResponse);
        });
        return lecturerResponses;
    }

    @Override
    public List<User> getAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM User ");
        return q.getResultList();
    }

    @Override
    public LecturerResponse updatePasswordLecturer(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(u);
        Query q = s.createQuery("SELECT l FROM Lecturer l WHERE l.userId = :userId");
        q.setParameter("userId", u.getId());
        Lecturer lecturer = (Lecturer) q.getSingleResult();
        lecturer.setIsLocked(false);
        s.update(lecturer);

        LecturerResponse lecturerResponse = LecturerResponse
                .builder()
                .avatarLink(u.getAvatar())
                .email(u.getEmail())
                .fullName(u.getFullName())
                .phone(u.getPhone())
                .isLocked(lecturer.getIsLocked())
                .username(u.getUsername())
                .displayName(u.getDisplayName())
                .password(u.getPassword())
                .createdOn(lecturer.getCreatedOn())
                .userId(u.getId())
                .id(lecturer.getId())
                .build();
        return lecturerResponse;
    }

    @Override
    public List<Object[]> getAllLecturersNotUpdateNewPassword() {
        Session s = this.factory.getObject().getCurrentSession();
        String defaultPass = "ou@123";
        String sql = "Select u.id, u.password from lecturer l\n" +
                "    INNER JOIN users u ON u.id = l.users_id\n" +
                "WHERE HOUR(TIMEDIFF(CURRENT_TIMESTAMP, l.created_on)) > 24 and l.is_locked=false";
        Query q = s.createNativeQuery(sql);
        List<Object[]> users = q.getResultList();
        List<Object[]> usersUpdate = new ArrayList<>();
        users.forEach(u -> {
            if (passwordEncoder.matches(defaultPass, u[1].toString())) {
                usersUpdate.add(u);
            }
        });
        return usersUpdate;
    }

    @Override
    public Boolean lockLecturerWithoutChangeDefaultPassword() {
        Session s = this.factory.getObject().getCurrentSession();
        List<Object[]> usersUpdate = this.getAllLecturersNotUpdateNewPassword();
        try {
            usersUpdate.forEach(u -> {
                String sql = "UPDATE lecturer l\n" +
                        "    INNER JOIN users u ON u.id = l.users_id\n" +
                        "SET l.is_locked = 1\n" +
                        "WHERE u.id=:userId";
                Query q = s.createNativeQuery(sql);
                q.setParameter("userId", u[0]);
                q.executeUpdate();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean deleteUser(User user) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("DELETE FROM User u WHERE u.id=:userId");
        q.setParameter("userId", user.getId());
        int rowsAffected = q.executeUpdate();

        return rowsAffected > 0;
    }

    @Override
    public List<Role> getAllRoleOfUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT r FROM Role r JOIN UsersRoles ur on ur.role.id=r.id JOIN User u ON u.id=ur.user.id WHERE u.id=:userId");
        q.setParameter("userId", u.getId());

        return q.getResultList();
    }

    @Override
    public Boolean confirmAlumni(Alumni alumni) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("UPDATE Alumni a SET a.isConfirmed = true WHERE a.id=:alumniId");
        q.setParameter("alumniId", alumni.getId());
        return q.executeUpdate() > 0;
    }

    @Override
    public Boolean checkAlumniIsConfirmed(Alumni alumni) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT a FROM Alumni a WHERE a.id=:alumniId");
        q.setParameter("alumniId", alumni.getId());
        Alumni alumniSaved = (Alumni) q.getSingleResult();
        return alumniSaved.getIsConfirmed();
    }

    @Override
    public Boolean checkLecturerIsLocked(Lecturer lecturer) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT l FROM Lecturer l WHERE l.id=:lecturerId");
        q.setParameter("lecturerId", lecturer.getId());
        Lecturer lecturerSaved = (Lecturer) q.getSingleResult();
        return lecturerSaved.getIsLocked();
    }
}
