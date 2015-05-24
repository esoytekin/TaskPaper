package com.bau.rest.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bau.rest.dao.UserDAO;
import com.bau.rest.entity.User;

@Repository
@Transactional
public class UserHibernateDAO implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void save(User entity) {
		getCurrentSession().save(entity);
	}

	@Override
	public void update(User entity) {
		getCurrentSession().update(entity);
	}

	@Override
	public void delete(User entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public User getById(Long id) {
		return (User) getCurrentSession().get(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getList() {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		return criteria.list();
	}
	
	@Override
	public User getByUsername(String username) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("username", username));
		return (User) criteria.uniqueResult();
	}
	

}
