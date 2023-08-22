package com.lqt.repository.impl;

import com.lqt.dto.PostStatsResponse;
import com.lqt.dto.StatsUserResponse;
import com.lqt.repository.StatisticRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return ((BigInteger) q.getSingleResult()).intValue();
    }

    @Override
    public int countInteractOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(i.id) FROM interactions i where posts_id=:postId");
        q.setParameter("postId", postId);
        return ((BigInteger) q.getSingleResult()).intValue();
    }

    @Override
    public int countShareOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(s.id) FROM shares s where posts_id=:postId");
        q.setParameter("postId", postId);
        return ((BigInteger) q.getSingleResult()).intValue();
    }

    @Override
    public int countAllUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(*) FROM users");
        return ((BigInteger) q.getSingleResult()).intValue();
    }

    @Override
    public int countAllGroups() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT COUNT(*) FROM `groups`");

        return ((BigInteger)q.getSingleResult()).intValue();
    }

    @Override
    public int countNumberOfUserInGroup(Long groupId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNativeQuery("SELECT count(*) FROM groups_members where group_id=:groupId");
        q.setParameter("groupId", groupId);
        return ((BigInteger)q.getSingleResult()).intValue();
    }

    @Override
    public int countNumberOfPosts() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT COUNT(*) FROM Post");
        return ((Long)q.getSingleResult()).intValue();
    }

    @Override
    public List<PostStatsResponse> statsNumberOfPosts(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "";
        Map<String, Object> parameters = new HashMap<>();
        if (params.containsKey("year") && !params.get("year").isEmpty()) {
            if (params.containsKey("month") && !params.get("month").isEmpty()) {
                parameters.put("year", Integer.parseInt(params.get("year")));
                parameters.put("month", Integer.parseInt(params.get("month")));
                sql = "SELECT DAY(p.timestamp), COUNT(*) FROM posts p WHERE YEAR(p.timestamp) = :year " +
                        "AND MONTH(p.timestamp) = :month GROUP BY DAY(p.timestamp) ORDER BY DAY(p.timestamp)";
            }else if (params.containsKey("quarter") && !params.get("quarter").isEmpty()) {
                parameters.put("year", Integer.parseInt(params.get("year")));
                if (Integer.parseInt(params.get("quarter")) == 1) {
                    parameters.put("startMonth", 1);
                    parameters.put("endMonth", 3);
                } else if (Integer.parseInt(params.get("quarter")) == 2) {
                    parameters.put("startMonth", 4);
                    parameters.put("endMonth", 6);
                } else if (Integer.parseInt(params.get("quarter")) == 3) {
                    parameters.put("startMonth", 7);
                    parameters.put("endMonth", 9);
                }else {
                    parameters.put("startMonth", 10);
                    parameters.put("endMonth", 12);
                }

                sql = "SELECT MONTH(p.timestamp), COUNT(*) FROM posts p " +
                        "WHERE YEAR(p.timestamp) = :year AND MONTH(p.timestamp) " +
                        "BETWEEN :startMonth AND :endMonth GROUP BY MONTH(p.timestamp) ORDER BY MONTH(p.timestamp)";
            } else {
                parameters.put("year", Integer.parseInt(params.get("year")));
                sql = "SELECT MONTH(p.timestamp), COUNT(*) FROM posts p " +
                        "WHERE YEAR(p.timestamp) = :year GROUP BY MONTH(p.timestamp) ORDER BY MONTH(p.timestamp)";
            }
        }else {
            parameters.put("year", LocalDate.now().getYear());
            if (params.containsKey("month") && !params.get("month").isEmpty()) {
                parameters.put("month", Integer.parseInt(params.get("month")));
                sql = "SELECT DAY(p.timestamp), COUNT(*) FROM posts p WHERE YEAR(p.timestamp) = :year " +
                        "AND MONTH(p.timestamp) = :month GROUP BY DAY(p.timestamp) ORDER BY DAY(p.timestamp)";
            } else if (params.containsKey("quarter") && !params.get("quarter").isEmpty()) {
                if (Integer.parseInt(params.get("quarter")) == 1) {
                    parameters.put("startMonth", 1);
                    parameters.put("endMonth", 3);
                } else if (Integer.parseInt(params.get("quarter")) == 2) {
                    parameters.put("startMonth", 4);
                    parameters.put("endMonth", 6);
                } else if (Integer.parseInt(params.get("quarter")) == 3) {
                    parameters.put("startMonth", 7);
                    parameters.put("endMonth", 9);
                }else {
                    parameters.put("startMonth", 10);
                    parameters.put("endMonth", 12);
                }
                sql = "SELECT MONTH(p.timestamp), COUNT(*) FROM posts p " +
                        "WHERE YEAR(p.timestamp) = :year AND MONTH(p.timestamp) " +
                        "BETWEEN :startMonth AND :endMonth GROUP BY MONTH(p.timestamp) ORDER BY MONTH(p.timestamp)";
            } else {
                sql = "SELECT MONTH(p.timestamp), COUNT(*) FROM posts p " +
                        "WHERE YEAR(p.timestamp) = :year GROUP BY MONTH(p.timestamp) ORDER BY MONTH(p.timestamp)";
            }
        }
        Query query = s.createNativeQuery(sql);
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        List<PostStatsResponse> response = new ArrayList<>();
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
                String columnName = result[0].toString();
                Integer columnValue = ((BigInteger)result[1]).intValue();

                PostStatsResponse postStatsResponse = new PostStatsResponse();
                postStatsResponse.setLabel(columnName);
                postStatsResponse.setData(columnValue);
                response.add(postStatsResponse);
        }

        return response;
    }

    @Override
    public List<StatsUserResponse> statsUser() {
        Session s = this.factory.getObject().getCurrentSession();
        String sql = "SELECT\n" +
                "    category,\n" +
                "    ROUND((count * 100 / total_users), 2) AS percentage\n" +
                "FROM\n" +
                "    (\n" +
                "        SELECT\n" +
                "            'alumni' AS category,\n" +
                "            COUNT(*) AS count\n" +
                "        FROM\n" +
                "            alumni\n" +
                "        JOIN\n" +
                "            users ON alumni.users_id = users.id\n" +
                "        UNION ALL\n" +
                "        SELECT\n" +
                "            'lecturer' AS category,\n" +
                "            COUNT(*) AS count\n" +
                "        FROM\n" +
                "            lecturer\n" +
                "        JOIN\n" +
                "            users ON lecturer.users_id = users.id\n" +
                "        UNION ALL\n" +
                "        SELECT\n" +
                "            'other' AS category,\n" +
                "            COUNT(*) AS count\n" +
                "        FROM\n" +
                "            users\n" +
                "        WHERE\n" +
                "            users.id NOT IN (\n" +
                "                SELECT users_id FROM alumni\n" +
                "                UNION\n" +
                "                SELECT users_id FROM lecturer\n" +
                "            )\n" +
                "    ) AS result\n" +
                "CROSS JOIN\n" +
                "    (\n" +
                "        SELECT COUNT(*) AS total_users FROM users\n" +
                "    ) AS total";
        List<StatsUserResponse> responses = new ArrayList<>();
        Query query = s.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
                String columnName = result[0].toString();
                double columnValue = ((BigDecimal)result[1]).doubleValue();

                StatsUserResponse statsUserResponse = new StatsUserResponse();
                statsUserResponse.setName(columnName);
                statsUserResponse.setPercent(columnValue);
                responses.add(statsUserResponse);
        }
        return responses;
    }
}
