package com.lqt.repository.impl;

import com.lqt.pojo.Interaction;
import com.lqt.repository.InteractionRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class InteractionRepositoryImpl implements InteractionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Interaction interactWithPost(Interaction interaction) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(interaction);
        return interaction;
    }

    @Override
    public List<Interaction> countInteractWithPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Interaction i WHERE i.postId = :postId");
        q.setParameter("postId", postId);
        return q.getResultList();
    }
}
