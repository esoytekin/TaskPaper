package com.bau.rest.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bau.rest.dao.CategoryDAO;
import com.bau.rest.entity.Category;
import com.bau.rest.entity.User;

@Repository
@Transactional
public class CategoryHibernateDAO implements CategoryDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	

	@Override
	public void save(Category entity) {
		getCurrentSession().save(entity);
		
	}

	@Override
	public void update(Category entity) {
		getCurrentSession().update(entity);
	}

	@Override
	public void delete(Category entity) {
		getCurrentSession().delete(entity);
	}

	@Override
	public Category getById(Long id) {
		return (Category) getCurrentSession().get(Category.class, id);
	}
	
	@Override
	public Category getByName(String name,User user) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("user", user));
		return (Category) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getList() {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getListByUser(User user) {
		Criteria criteria = getCurrentSession().createCriteria(Category.class);
		criteria.add(Restrictions.eq("user", user));
		criteria.addOrder(Order.asc("order"));
		return criteria.list();
	}

}
