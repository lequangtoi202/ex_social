package com.lqt.repository.impl;

import com.lqt.dto.UserDto;
import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Group;
import com.lqt.pojo.GroupsMembers;
import com.lqt.pojo.User;
import com.lqt.repository.GroupRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class GroupRepositoryImpl implements GroupRepository {
    @Autowired
    private LocalSessionFactoryBean factory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Group create(Group group) {
        Session s = this.factory.getObject().getCurrentSession();
        Group groupSave = Group.builder()
                .groupName(group.getGroupName())
                .creatorId(group.getCreatorId())
                .build();
        String tableName = "`groups`";
        s.createNativeQuery("INSERT INTO " + tableName + " (creator_id, group_name) VALUES (?, ?)")
                .setParameter(1, groupSave.getCreatorId())
                .setParameter(2, groupSave.getGroupName())
                .executeUpdate();
        return groupSave;
    }

    @Override
    public Group update(Group groupUpdate) {
        Session s = this.factory.getObject().getCurrentSession();
        String sqlQuery = "UPDATE `groups` SET group_name = :groupName, creator_id = :creatorId WHERE id = :id";
        Query query = s.createNativeQuery(sqlQuery);
        query.setParameter("groupName", groupUpdate.getGroupName());
        query.setParameter("creatorId", groupUpdate.getCreatorId());
        query.setParameter("id", groupUpdate.getId());
        int rowsAffected = query.executeUpdate();

        if (rowsAffected > 0) {
            return groupUpdate;
        } else {
            return null;
        }
    }


    @Override
    public Boolean delete(Group group) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("DELETE FROM `groups` where id=:id");
        q.setParameter("id", group.getId());
        if (q.executeUpdate() > 0)
            return true;
        return false;
    }

    @Override
    public Group findById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        String tableName = "`groups`";
        String sqlQuery = "SELECT * FROM " + tableName + " WHERE id = :id";
        TypedQuery<Group> q = (TypedQuery<Group>) entityManager.createNativeQuery(sqlQuery, Group.class);
        q.setParameter("id", id);
        Group group = q.getSingleResult();
        return group;
    }

    @Override
    public List<Group> getAllGroups(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        String sqlQuery = "SELECT * FROM `groups` g WHERE g.group_name like CONCAT('%', :name, '%')";
        TypedQuery<Group> q = (TypedQuery<Group>) entityManager.createNativeQuery(sqlQuery, Group.class);
        q.setParameter("name", name);
        List<Group> groups = q.getResultList();
        return groups;
    }

    @Override
    public List<Group> getAllGroupsByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        String sqlQuery = "SELECT * FROM `groups` WHERE creator_id = :userId";
        TypedQuery<Group> q = (TypedQuery<Group>) entityManager.createNativeQuery(sqlQuery, Group.class);
        q.setParameter("userId", userId);
        return q.getResultList();
    }

    @Override
    public Boolean addMemberToGroup(GroupsMembers groupsMembers) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            s.save(groupsMembers);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public GroupsMembers findMemberInGroupByUserIdAndGroupId(Long userId, Long groupId) {
        Session s = this.factory.getObject().getCurrentSession();
        User user = s.get(User.class, userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        Group group = this.findById(groupId);
        if (group == null) {
            throw new ResourceNotFoundException("Group", "id", groupId);
        }
        Query q = s.createQuery("SELECT gm FROM GroupsMembers gm WHERE gm.userId=:userId and gm.group.id=:groupId");
        q.setParameter("userId", userId);
        q.setParameter("groupId", groupId);
        try {
            return (GroupsMembers) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Boolean deleteMemberFromGroup(GroupsMembers groupsMembers) {
        Session s = this.factory.getObject().getCurrentSession();
        try {
            s.delete(groupsMembers);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> getAllMailOfGroup(Long groupId) {
        Session s = this.factory.getObject().getCurrentSession();
        Group group = this.findById(groupId);
        if (group == null) {
            throw new ResourceNotFoundException("Group", "id", groupId);
        }
        Query q = s.createNativeQuery("SELECT u.email \n" +
                "FROM `groups` g \n" +
                "JOIN groups_members gm ON gm.group_id = g.id \n" +
                "JOIN users u ON u.id = gm.user_id \n" +
                "WHERE g.id = :groupId");
        q.setParameter("groupId", group.getId());
        return q.getResultList();
    }

    @Override
    public List<String> getAllEmailOfAlumni() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT u.email \n" +
                "FROM `groups` g \n" +
                "JOIN groups_members gm ON gm.group_id = g.id \n" +
                "JOIN users u ON u.id = gm.user_id;\n");
        return q.getResultList();
    }

    @Override
    public List<Group> getGroupByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        String tableName = "`groups`";
        String sql = "SELECT * FROM " + tableName + " WHERE group_name like '" + String.format("%%%s%%", name) + "'";
        TypedQuery<Group> query = (TypedQuery<Group>) entityManager.createNativeQuery(sql, Group.class);
        return query.getResultList();
    }

    @Override
    public List<User> getAllUsersOfGroup(Long groupId) {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "SELECT u.id, u.username, u.password, u.email, u.full_name, u.phone, u.avatar, u.bg_image, u.password_reset_token FROM users u INNER JOIN groups_members gm ON gm.user_id=u.id where gm.group_id=:groupId";
        Query q = s.createNativeQuery(sql)
                .setParameter("groupId", groupId);
        List<Object[]> rows = q.getResultList();
        List<User> userList = new ArrayList<>();

        for (Object[] row : rows) {
            User user = new User();
            user.setId(((BigInteger) row[0]).longValue());
            user.setUsername((String) row[1]);
            user.setPassword((String) row[2]);
            user.setEmail((String) row[3]);
            user.setFullName((String) row[4]);
            user.setPhone((String) row[5]);
            user.setAvatar((String) row[6]);
            user.setBackgroundImage((String) row[7]);
            user.setPasswordResetToken((String) row[8]);

            userList.add(user);
        }
        return userList;
    }
}
