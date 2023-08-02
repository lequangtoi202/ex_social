package com.lqt.repository.impl;

import com.lqt.pojo.Option;
import com.lqt.pojo.Response;
import com.lqt.repository.ResponseRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ResponseRepositoryImpl implements ResponseRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Response> getAllResponsesBySurveyId(Long surveyId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT r FROM Response r WHERE r.surveyId=:surveyId");
        q.setParameter("surveyId", surveyId);
        return q.getResultList();
    }

    @Override
    public List<Response> getAllResponsesBySurveyAndQuestionId(Long surveyId, Long questionId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT r FROM Response r WHERE r.surveyId=:surveyId and r.question.id=:questionId");
        q.setParameter("surveyId", surveyId);
        q.setParameter("questionId", questionId);
        return q.getResultList();
    }

    @Override
    public Response getResponseBySurveyAndQuestionIdAndResId(Long surveyId, Long questionId, Long resId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT r FROM Response r WHERE r.surveyId=:surveyId and r.question.id=:questionId and r.id=:resId");
        q.setParameter("surveyId", surveyId);
        q.setParameter("questionId", questionId);
        q.setParameter("resId", resId);
        return (Response) q.getSingleResult();
    }

    @Override
    public Response getByResponseId(Long resId) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Response.class, resId);
    }

    @Override
    public Response createResponseForQuestion(Response res) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(res);
        return res;
    }

    @Override
    public Response updateResponseForQuestion(Response res) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(res);
        return res;
    }

    @Override
    public Boolean deleteResponseForQuestion(Response res) {
        Session s = this.factory.getObject().getCurrentSession();
        s.delete(res);
        return true;
    }

    @Override
    public Option getOptionById(Long optId) {
        Session s = this.factory.getObject().getCurrentSession();
        Option option = s.get(Option.class, optId);

        return option;
    }
}
