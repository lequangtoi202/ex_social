package com.lqt.repository.impl;

import com.lqt.pojo.Survey;
import com.lqt.repository.SurveyRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class SurveyRepositoryImpl implements SurveyRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Survey create(Survey survey) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(survey);
        return survey;
    }

    @Override
    public Survey update(Survey survey) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(survey);
        return survey;
    }

    @Override
    public Boolean delete(Survey survey) {
        Session s = this.factory.getObject().getCurrentSession();
        s.delete(survey);
        return true;
    }

    @Override
    public Survey findById(Long surveyId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Survey.class, surveyId);
    }

    @Override
    public Survey findByPostId(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Survey s WHERE s.post.id =:postId");
        q.setParameter("postId", postId);
        return (Survey) q.getSingleResult();
    }

    @Override
    public List<Survey> getAllSurveys() {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Survey");
        return q.getResultList();
    }

    @Override
    public List<Survey> getAllSurveysByUserId(Long userId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Survey s WHERE s.userId=:userId");
        q.setParameter("userId", userId);
        return q.getResultList();
    }


    @Override
    public List<Survey> getAllSurveysByDate() {
        return null;
    }
}
