package com.lqt.repository.impl;

import com.lqt.pojo.Comment;
import com.lqt.repository.CommentRepository;
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
public class CommentRepositoryImpl implements CommentRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Comment save(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(comment);
        return comment;
    }

    @Override
    public Comment update(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(comment);
        return comment;
    }

    @Override
    public Boolean delete(Comment comment) {
        Session s = this.factory.getObject().getCurrentSession();
        s.delete(comment);
        return true;
    }

    @Override
    public Comment findCommentById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Comment.class, id);
    }

    @Override
    public List<Comment> findAllCommentsByPostId(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT c FROM Comment c WHERE c.postId=:postId");
        q.setParameter("postId", postId);
        return q.getResultList();
    }

    @Override
    public List<Comment> findAllCommentsByCommentParentId(Long postId, Long commentParentId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT c FROM Comment c WHERE c.belongsComment.id=:commentParentId and c.postId=:postId");
        q.setParameter("commentParentId", commentParentId);
        q.setParameter("postId", postId);
        try {
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
