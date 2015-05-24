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

import com.bau.rest.dao.TaskDAO;
import com.bau.rest.entity.Category;
import com.bau.rest.entity.Task;

@Repository
@Transactional
public class TaskHibernateDAO implements TaskDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void save(Task entity) {
		getCurrentSession().save(entity);
	}

	@Override
	public void update(Task entity) {
		getCurrentSession().update(entity);
		
	}

	@Override
	public void delete(Task entity) {
		getCurrentSession().delete(entity);
		
	}

	@Override
	public Task getById(Long id) {
		return (Task) getCurrentSession().get(Task.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getList() {
		Criteria criteria = getCurrentSession().createCriteria(Task.class);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> getByCategory(Category entity) {
		Criteria criteria = getCurrentSession().createCriteria(Task.class);
		criteria.add(Restrictions.eq("category", entity));
		criteria.addOrder(Order.desc("favorite"));
		criteria.addOrder(Order.desc("date"));
		return criteria.list();
	}

}
