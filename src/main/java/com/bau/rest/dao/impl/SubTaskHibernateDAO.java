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

import com.bau.rest.dao.SubTaskDAO;
import com.bau.rest.entity.SubTask;
import com.bau.rest.entity.Task;

@Repository
@Transactional
public class SubTaskHibernateDAO implements SubTaskDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	

	@Override
	public void save(SubTask entity) {
		getCurrentSession().save(entity);

	}

	@Override
	public void update(SubTask entity) {
		getCurrentSession().update(entity);

	}

	@Override
	public void delete(SubTask entity) {
		getCurrentSession().delete(entity);

	}

	@Override
	public SubTask getById(Long id) {
		return (SubTask) getCurrentSession().get(SubTask.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubTask> getList() {
		Criteria criteria = getCurrentSession().createCriteria(SubTask.class);
		criteria.addOrder(Order.desc("favorite"));
		criteria.addOrder(Order.desc("date"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SubTask> getByTask(Task entity) {
		Criteria criteria = getCurrentSession().createCriteria(SubTask.class);
		criteria.add(Restrictions.eq("task", entity));
		criteria.addOrder(Order.desc("favorite"));
		criteria.addOrder(Order.desc("date"));
		return criteria.list();
	}

}
