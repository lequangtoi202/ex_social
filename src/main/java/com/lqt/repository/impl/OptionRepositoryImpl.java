package com.lqt.repository.impl;

import com.lqt.pojo.Option;
import com.lqt.repository.OptionRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class OptionRepositoryImpl implements OptionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Option> getAllOptionsOfQuestion(Long questionId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Option o WHERE o.questionId=:questionId");
        q.setParameter("questionId", questionId);
        return q.getResultList();
    }
}
