package com.lqt.repository.impl;

import com.lqt.exception.ResourceNotFoundException;
import com.lqt.pojo.Option;
import com.lqt.pojo.Question;
import com.lqt.repository.QuestionRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class QuestionRepositoryImpl implements QuestionRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Question createQuestionForSurvey(Question question, List<Option> options) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(question);
        options.forEach(o -> {
            Option option = Option.builder()
                    .content(o.getContent())
                    .questionId(question.getId())
                    .build();
            s.save(option);
        });
        return question;
    }

    @Override
    public Question updateQuestionForSurvey(Question q) {
        Session s = this.factory.getObject().getCurrentSession();
        s.update(q);
        return q;
    }

    @Override
    public Boolean deleteQuestionForSurvey(Question q) {
        Session s = this.factory.getObject().getCurrentSession();
        s.delete(q);
        return true;
    }

    @Override
    public Boolean deleteAllOptionOfQuestion(Long questionId) {
        Session s = this.factory.getObject().getCurrentSession();
        Question question = s.get(Question.class, questionId);
        if (question == null) {
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        Query q = s.createQuery("DELETE FROM Option o WHERE o.questionId=:questionId");
        q.setParameter("questionId", questionId);
        if (q.executeUpdate() > 0)
            return true;
        return false;
    }

    @Override
    public List<Question> getAllQuestionBySurveyId(Long surveyId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT q FROM Question q WHERE q.surveyId=:surveyId");
        q.setParameter("surveyId", surveyId);
        return q.getResultList();
    }

    @Override
    public Question getQuestionById(Long questionId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT q FROM Question q WHERE q.id=:questionId");
        q.setParameter("questionId", questionId);
        return (Question) q.getSingleResult();
    }
}
