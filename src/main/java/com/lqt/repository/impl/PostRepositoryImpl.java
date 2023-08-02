package com.lqt.repository.impl;

import com.lqt.pojo.Post;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
@Transactional
public class PostRepositoryImpl implements com.lqt.repository.PostRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Post post(Post post) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(post);
        return post;
    }

    @Override
    public Post update(Post post) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(post);
        return post;
    }

    @Override
    public Boolean delete(Post post) {
        Session s = this.factory.getObject().getCurrentSession();
        s.delete(post);
        return true;
    }

    @Override
    public Post lockPost(Post post) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(post);
        return post;
    }

    @Override
    public Post findPostById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Post.class, id);
    }

    @Override
    public Post findPostByIdAndUserId(Long id, Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT p FROM Post p WHERE p.userId=:userId and p.id=:id");
        q.setParameter("userId", userId);
        q.setParameter("id", id);
        try {
            return (Post) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Post> findAllPosts() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Post");
        List<Post> posts = q.getResultList();
        return posts;
    }

    @Override
    public List<Post> findPostsByUserId(Long userId, String direction) {
        Session s = this.factory.getObject().getCurrentSession();
        String queryString = "FROM Post p WHERE p.userId = :userId ORDER BY ";
        if ("asc".equalsIgnoreCase(direction)) {
            queryString += "p.timestamp ASC";
        } else if ("desc".equalsIgnoreCase(direction)) {
            queryString += "p.timestamp DESC";
        } else {
            queryString += "p.timestamp DESC";
        }

        Query q = s.createQuery(queryString);
        q.setParameter("userId", userId);

        List<Post> posts = q.getResultList();
        return posts;
    }
}
