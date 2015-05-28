package com.bau.rest.dao;

import java.util.List;

import com.bau.rest.entity.Category;
import com.bau.rest.entity.Task;

public interface TaskDAO {
	void save(Task entity);
	void update(Task entity);
	void delete(Task entity);
	Task getById(Long id);
	List<Task> getList();
	List<Task> getByCategory(Category entity);
	long getTaskCountByCategory(Category entity);
}
