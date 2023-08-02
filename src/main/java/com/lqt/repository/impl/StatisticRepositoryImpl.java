package com.lqt.repository.impl;

import com.lqt.repository.StatisticRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Repository
@Transactional
public class StatisticRepositoryImpl implements StatisticRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public int countCommentOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(c.id) FROM comments c where posts_id=:postId");
        q.setParameter("postId", postId);
        return (int) q.getSingleResult();
    }

    @Override
    public int countInteractOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(i.id) FROM interactions i where posts_id=:postId");
        q.setParameter("postId", postId);
        return (int) q.getSingleResult();
    }

    @Override
    public int countShareOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(s.id) FROM shares s where posts_id=:postId");
        q.setParameter("postId", postId);
        return (int) q.getSingleResult();
    }

    @Override
    public int countAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(*) FROM users");
        return (int) q.getSingleResult();
    }

    @Override
    public int countNumberOfUserInGroup(Long groupId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(*) FROM groups_members where group_id=:groupId");
        q.setParameter("groupId", groupId);
        return (int) q.getSingleResult();
    }

    @Override
    public int countNumberOfPosts(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        StringBuffer hql = new StringBuffer("SELECT COUNT(*) FROM Post WHERE 1=1");
        Map<String, Object> parameters = new HashMap<>();
        if (params.containsKey("year")) {
            hql.append(" AND YEAR(timestamp) = :year");
            parameters.put("year", Integer.parseInt(params.get("year")));
        }

        if (params.containsKey("month")) {
            if (!parameters.isEmpty()) {
                hql.append(" AND");
            }
            hql.append(" AND MONTH(timestamp) = :month");
            parameters.put("month", Integer.parseInt(params.get("month")));
        } else if (params.containsKey("quarter")) {
            if (!parameters.isEmpty()) {
                hql.append(" AND");
            }
            hql.append(" AND QUARTER(timestamp) = :quarter");
            parameters.put("quarter", Integer.parseInt(params.get("quarter")));
        }

        Query<Integer> query = s.createQuery(hql.toString(), Integer.class);
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }

        Integer count = query.uniqueResult();
        return count != null ? count.intValue() : 0;
    }
}
