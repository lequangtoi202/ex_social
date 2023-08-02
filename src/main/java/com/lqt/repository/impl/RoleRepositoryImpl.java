package com.lqt.repository.impl;

import com.lqt.pojo.Role;
import com.lqt.repository.RoleRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;


@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Role save(Role role) {
        Session s = this.factory.getObject().getCurrentSession();
        s.save(role);
        return role;
    }

    @Override
    public Role update(Role role, Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Role roleSaved = this.getRoleById(id);
        roleSaved.setName(role.getName());
        s.save(roleSaved);
        return roleSaved;
    }

    @Override
    public boolean delete(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Role roleSaved = this.getRoleById(id);
        try {
            s.delete(roleSaved);
            return true;
        }catch(Exception e){
            System.out.println("RoleRepository: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Role getRoleByName(String name) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createQuery("SELECT r FROM Role r WHERE r.name=:name");
        q.setParameter("name", name);
        q.setMaxResults(1);
        Role role = null;
        try {
            role = (Role) q.getSingleResult();
            return role;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Role getRoleById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        Role roleSaved = s.get(Role.class, id);
        return roleSaved;
    }
}
