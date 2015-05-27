package com.bau.rest.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bau.rest.dao.UserRoleDAO;
import com.bau.rest.entity.UserRole;

@Repository
@Transactional
public class UserRoleHibernateDAO implements UserRoleDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	@Override
	public UserRole getById(Long id) {

		return (UserRole)getCurrentSession().get(UserRole.class, id);
	}

	@Override
	public UserRole getByUsername(String username) {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("username", username));
		return (UserRole) criteria.uniqueResult();
	}

	@Override
	public void save(UserRole entity) {
		getCurrentSession().save(entity);
	}

	@Override
	public void update(UserRole entity) {
		getCurrentSession().update(entity);
	}

	@Override
	public void delete(UserRole entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> getUserRoles() {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		return criteria.list();
	}

}
