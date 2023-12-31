package com.lqt.repository.impl;

import com.lqt.pojo.Share;
import com.lqt.repository.ShareRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ShareRepositoryImpl implements ShareRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Share sharePost(Share share) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(share);
        return share;
    }

    @Override
    public List<Share> getAllShareOfPost(Long postId) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("FROM Share s WHERE s.post.id = :postId");
        q.setParameter("postId", postId);
        return q.getResultList();
    }
}
